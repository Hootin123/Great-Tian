package com.xtr.core.service.account;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerMoneyRecordsBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.MethodUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.core.persistence.reader.account.SubAccountReaderMapper;
import com.xtr.core.persistence.writer.account.RedEnvelopeWriterMapper;
import com.xtr.core.persistence.writer.account.SubAccountWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyMoneyRecordsWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyRechargesWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerMoneyRecordsWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.xtr.api.service.shebao.CompanyShebaoService;

/**
 * <p>客户子账户</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/3 13:33
 */
@Service("subAccountService")
public class SubAccountServiceImpl implements SubAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubAccountServiceImpl.class);

    @Resource
    private SubAccountWriterMapper subAccountWriterMapper;

    @Resource
    private SubAccountReaderMapper subAccountReaderMapper;

    @Resource
    private CustomerMoneyRecordsWriterMapper customerMoneyRecordsWriterMapper;

    @Resource
    private CompanyMoneyRecordsWriterMapper companyMoneyRecordsWriterMapper;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanyShebaoOrderWriterMapper companyShebaoOrderWriterMapper;

    @Resource
    private RedEnvelopeWriterMapper redEnvelopeWriterMapper;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private CompanyRechargesWriterMapper companyRechargesWriterMapper;
    /**
     * 新增子账户
     *
     * @param subAccountBean
     * @return
     */
    public int insert(SubAccountBean subAccountBean) throws Exception {
        subAccountBean.setAmout(new BigDecimal("0.00"));
        subAccountBean.setCashAmout(new BigDecimal("0.00"));
        subAccountBean.setUncashAmount(new BigDecimal("0.00"));
        subAccountBean.setFreezeCashAmount(new BigDecimal("0.00"));
        subAccountBean.setFreezeUncashAmount(new BigDecimal("0.00"));
        subAccountBean.setHash(MethodUtil.MD5(UUID.randomUUID().toString()));
        subAccountBean.setCheckValue(getMd5Code(subAccountBean));
        //状态 00-生效,01-冻结,02-注销
        subAccountBean.setState("00");
        //余额是否自动转入 0:非自动 1;自动
        subAccountBean.setIsAutoTransfer(1);
        subAccountBean.setCreateTime(new Date());
        subAccountBean.setUpdateTime(new Date());
        return subAccountWriterMapper.insert(subAccountBean);
    }


    /**
     * 处理账户业务之前，检查账户状态及校验值
     *
     * @throws BusinessException
     */
    public void checkAccountStatus(SubAccountBean subAccountBean) throws BusinessException {

        if (subAccountBean == null) {
            throw new BusinessException("账户未开通");
        }//00-生效,01-冻结,02-注销
        else if (StringUtils.equals("01", subAccountBean.getState())) {
            throw new BusinessException("用户ID【" + subAccountBean.getCustId() + "】账户已锁定");
        } else if (StringUtils.equals("02", subAccountBean.getState())) {
            throw new BusinessException("用户ID【" + subAccountBean.getCustId() + "】账户已注销");
        }

        try {
            //检查MD5值
            String md5Code=getMd5Code(subAccountBean);
            LOGGER.info("检查MD5值,生成的MD5:"+md5Code);
            if (md5Code.equals(subAccountBean.getCheckValue())) {
                //成功
            } else {

                LOGGER.info(" getMd5Code >>> " + getMd5Code(subAccountBean));
                LOGGER.info(" getCheckValue >>> " + subAccountBean.getCheckValue());

                //锁定账户
                subAccountBean.setState("01");
                //更新账户状态
                subAccountWriterMapper.updateByPrimaryKeySelective(subAccountBean);

                LOGGER.error("用户ID【" + subAccountBean.getCustId() + "】账户数据被篡改");
                throw new BusinessException("用户ID【" + subAccountBean.getCustId() + "】账户数据被篡改");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 处理账户业务成功之后，更新账户校验值
     *
     * @param subAccountBean
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAccountCheckValue(SubAccountBean subAccountBean) throws BusinessException {
        if (subAccountBean.getFreezeCashAmount().doubleValue() < 0) {
            throw new BusinessException("更新账户可提现金额失败");
        }
        if (subAccountBean.getAmout().doubleValue() < 0) {
            throw new BusinessException("更新账户总金额失败");
        }
        if (subAccountBean.getCashAmout().doubleValue() < 0) {
            throw new BusinessException("更新账户可用金额失败");
        }
        try {
            subAccountBean.setHash(MethodUtil.MD5(UUID.randomUUID().toString()));
            subAccountBean.setCheckValue(getMd5Code(subAccountBean));
            int result = subAccountWriterMapper.updateByPrimaryKeySelective(subAccountBean);
            if (result <= 0) {
                throw new BusinessException("更新账户校验码失败");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 获取MD5
     *
     * @param subAccountBean
     * @return
     */
    public String getMd5Code(SubAccountBean subAccountBean) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(subAccountBean.getCustId())
                .append(subAccountBean.getAmout())
                .append(subAccountBean.getCashAmout())
                .append(subAccountBean.getUncashAmount())
                .append(subAccountBean.getFreezeCashAmount())
                .append(subAccountBean.getFreezeUncashAmount())
                .append(subAccountBean.getHash())
                .append(PropertyUtils.getString("accountKey"));

        LOGGER.debug("sb=" + sb.toString());

        return MethodUtil.MD5(sb.toString());
    }

    /**
     * 账户充值 生成流水
     *
     * @param custId
     * @param amount
     * @param recordSource 资金变动来源
     * @param resourceId   资金变动来源 ID
     * @param property     1-个人 2-企业
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rechargeRecords(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException {

        try {
            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);

            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else {
                //账户充值，加款动作
                recharge(custId, amount, resourceId, property);
                //插入资金流水
                insertMoneyRecords(custId, amount, 1, recordSource, resourceId, property);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * 账户充值不生成流水
     *
     * @param custId
     * @param amount
     * @param resourceId
     * @param property   1-个人 2-企业
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void recharge(Long custId, BigDecimal amount, Long resourceId, int property) throws BusinessException {

        try {
            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);

            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else {
                //变动后金额
                BigDecimal subAmout = subAccountBean.getAmout().add(amount);
                BigDecimal cashAmout = subAccountBean.getCashAmout().add(amount);

                subAccountBean.setAmout(subAmout);
                subAccountBean.setCashAmout(cashAmout);
                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * 贷款充值--充值的金额为冻结不可用
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void loadRecharge(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException{
        try{
            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);
            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("充值金额必须大于零");
            }else{
//                BigDecimal subAmout = subAccountBean.getAmout().add(amount);//充值用的余额

                BigDecimal uncashAmount = subAccountBean.getUncashAmount();
                if(uncashAmount == null)//借款余额
                    uncashAmount = new BigDecimal(0);
                uncashAmount = uncashAmount.add(amount);
                subAccountBean.setUncashAmount(uncashAmount);
                //不可提现冻结金额
//                BigDecimal freezeUncashAmount = subAccountBean.getFreezeUncashAmount().add(amount);

//                subAccountBean.setAmout(subAmout);
                BigDecimal amout = subAccountBean.getAmout();
                if(amount == null)
                    amount = new BigDecimal(0);
                amout = amout.add(amount);
                subAccountBean.setAmout(amout);//总金额


//                subAccountBean.setFreezeUncashAmount(freezeUncashAmount);
                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);
                //插入资金流水
                insertMoneyRecords(custId, amount, 1, recordSource, resourceId, property);
            }
        }catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 插入资金流水
     *
     * @param custId
     * @param amount
     * @param recordType
     * @param recordSource
     * @param resourceId
     * @param property
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertMoneyRecords(Long custId, BigDecimal amount,
                                   Integer recordType, Integer recordSource, Long resourceId, int property) throws BusinessException {
        try {
            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);
            BigDecimal moneyNow = subAccountBean.getAmout();

            if (subAccountBean.getProperty() == 1) {
                //个人账户 插入资金流水
                insertCustomerMoneyRecords(custId, moneyNow, amount, recordType, recordSource, resourceId);
            } else {
                //企业账户 插入资金流水
                insertCompanyMoneyRecords(custId, moneyNow, amount, recordType, recordSource, resourceId);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 更新资金流水
     *
     * @param custId
     * @param amount
     * @param resourceId
     * @param state
     */
    /*@Transactional(propagation = Propagation.REQUIRED)
    public void updateMoneyRecords(Long custId, BigDecimal amount, Long resourceId, Integer state) throws BusinessException {
        //获取账户信息
        SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, 2);
        //验证账户状态是否正常
        try {
            if (!checkAccountStatus(subAccountBean).isSuccess()) {
                return;
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        CompanyMoneyRecordsBean companyMoneyRecordsBean = companyMoneyRecordsReaderMapper.selectByResourceId(resourceId);
        if (null != companyMoneyRecordsBean) {
            companyMoneyRecordsBean.setRecordState(state);
            companyMoneyRecordsBean.setRecordMoneyNow(subAccountBean.getAmout());
            companyMoneyRecordsWriterMapper.updateByPrimaryKey(companyMoneyRecordsBean);
        }

    }*/


    /**
     * 账户扣款
     *
     * @param custId
     * @param amount
     * @param property
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deduct(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException {

        try {

            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else if (subAccountBean.getCashAmout().compareTo(amount) < 0) {
                throw new BusinessException("可用余额不足");
            } else if (subAccountBean.getAmout().compareTo(amount) < 0) {
                throw new BusinessException("余额不足");
            }  else {

                //扣款后金额
                subAccountBean.setAmout(subAccountBean.getAmout().subtract(amount));
                subAccountBean.setCashAmout(subAccountBean.getCashAmout().subtract(amount));

                BigDecimal moneyNow = subAccountBean.getAmout();
                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);

                if (subAccountBean.getProperty() == 1) {
                    //个人账户 插入资金流水
                    insertCustomerMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                } else {
                    //企业账户 插入资金流水
                    insertCompanyMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                }

            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 账户扣款
     *
     * @param custId
     * @param amount
     * @param property
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deduct(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property,BigDecimal prementAmount) throws BusinessException {

        try {
            //获取账户信息
            SubAccountBean subAccountBean = subAccountWriterMapper.selectByCustId(custId, property);
            LOGGER.info("扣款前获取账户信息:"+ JSON.toJSONString(subAccountBean));
//            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
//            LOGGER.info("扣款前获取账户信息:"+JSON.toJSONString(subAccountBean));
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else if (subAccountBean.getCashAmout().compareTo(amount) < 0) {
                throw new BusinessException("可用余额不足");
            } else if (subAccountBean.getAmout().compareTo(amount) < 0) {
                throw new BusinessException("余额不足");
            } else if (subAccountBean.getUncashAmount().compareTo(prementAmount) < 0) {
                throw new BusinessException("垫付金额余额不足");
            } else {

                //扣款后金额
                subAccountBean.setAmout(subAccountBean.getAmout().subtract(amount.add(prementAmount)));
                subAccountBean.setCashAmout(subAccountBean.getCashAmout().subtract(amount));
                subAccountBean.setUncashAmount(subAccountBean.getUncashAmount().subtract(prementAmount));

                BigDecimal moneyNow = subAccountBean.getAmout();
                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);

                if (subAccountBean.getProperty() == 1) {
                    //个人账户 插入资金流水
                    insertCustomerMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                } else {
                    //企业账户 插入资金流水
                    insertCompanyMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                    if(prementAmount.compareTo(new BigDecimal("0"))>0){
                        insertCompanyMoneyRecords(custId, moneyNow, prementAmount, 2, recordSource, resourceId);
                    }

                }

            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 账户资金冻结
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void frozen(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException {

        try {

            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
            LOGGER.info("账户信息：" + JSON.toJSONString(subAccountBean));
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else if (subAccountBean.getCashAmout().compareTo(amount) < 0) {
                throw new BusinessException("账户ID【" + subAccountBean.getId() + "】可提现余额不足");
            } else {

                BigDecimal cashAmout = subAccountBean.getCashAmout().subtract(amount);
                BigDecimal freezeCashAmout = subAccountBean.getFreezeCashAmount().add(amount);

                //扣款后可用金额
                subAccountBean.setCashAmout(cashAmout);
                //冻结金额
                subAccountBean.setFreezeCashAmount(freezeCashAmout);
                // 当前金额
                BigDecimal moneyNow = subAccountBean.getAmout();

                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);

                if (subAccountBean.getProperty() == 1) {
                    //个人账户 插入资金流水
                    insertCustomerMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                } else {
                    //企业账户 插入资金流水
                    insertCompanyMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                }
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 账户资金解冻返还
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void backwash(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException {
        try {

            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else if (subAccountBean.getFreezeCashAmount().compareTo(amount) < 0) {
                throw new BusinessException("账户ID【" + subAccountBean.getId() + "】可提现冻结余额不足");
            } else {

                BigDecimal cashAmout = subAccountBean.getCashAmout().add(amount);
                BigDecimal freezeCashAmount = subAccountBean.getFreezeCashAmount().subtract(amount);

                //扣款后金额
                subAccountBean.setCashAmout(cashAmout);
                //冻结金额
                subAccountBean.setFreezeCashAmount(freezeCashAmount);
                // 当前金额
                BigDecimal moneyNow = subAccountBean.getAmout();

                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);

                if (subAccountBean.getProperty() == 1) {
                    //个人账户 插入资金流水
                    insertCustomerMoneyRecords(custId, moneyNow, amount, 1, recordSource, resourceId);
                } else {
                    //企业账户 插入资金流水
                    insertCompanyMoneyRecords(custId, moneyNow, amount, 1, recordSource, resourceId);
                }

            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 账户资金解冻扣款
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void thawDeduct(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException {
        try {
            //获取账户信息
            SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(custId, property);
            //验证账户状态是否正常
            checkAccountStatus(subAccountBean);

            // 当前金额
            BigDecimal moneyNow = subAccountBean.getAmout();

            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new BusinessException("金额必须大于零");
            } else if (subAccountBean.getFreezeCashAmount().compareTo(amount) < 0) {
                throw new BusinessException("账户ID【" + subAccountBean.getId() + "】可提现冻结余额不足");
            } else if (moneyNow.compareTo(amount) < 0) {
                throw new BusinessException("账户ID【" + subAccountBean.getId() + "】余额不足");
            } else {
                //可提现冻结金额
                BigDecimal freezeCash = subAccountBean.getFreezeCashAmount().subtract(amount);
                BigDecimal amout = subAccountBean.getAmout().subtract(amount);
                subAccountBean.setFreezeCashAmount(freezeCash);
                subAccountBean.setAmout(amout);

                //更新账户余额及MD5值
                updateAccountCheckValue(subAccountBean);

                // 更新当前余额
                moneyNow = subAccountBean.getAmout();

                if (subAccountBean.getProperty() == 1) {
                    //个人账户 插入资金流水
                    insertCustomerMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                } else {
                    //企业账户 插入资金流水
                    insertCompanyMoneyRecords(custId, moneyNow, amount, 2, recordSource, resourceId);
                }
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * 插入企业账户资金流水
     *
     * @param custId
     * @param moneyNow
     * @param amount       企业资金变动 正为收入 负为支出
     * @param recordType   记录类型 1收入 2支付
     * @param recordSource 记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+
     */
    private void insertCompanyMoneyRecords(Long custId, BigDecimal moneyNow, BigDecimal amount,
                                           Integer recordType, Integer recordSource, Long resourceId) {
        CompanyMoneyRecordsBean companyMoneyRecordsBean = new CompanyMoneyRecordsBean();
        //企业Id
        companyMoneyRecordsBean.setRecordCompanyId(custId);
        //企业资金变动 正为收入 负为支出
        companyMoneyRecordsBean.setRecordMoney(amount);
        //当前剩余
        companyMoneyRecordsBean.setRecordMoneyNow(moneyNow);

        //记录类型 1收入 2支付
        companyMoneyRecordsBean.setRecordType(recordType);
        //记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+8缴社保
        companyMoneyRecordsBean.setRecordSource(recordSource);
        //记录产生来源Id
        companyMoneyRecordsBean.setResourceId(resourceId);
        //记录产生时间
        companyMoneyRecordsBean.setRecordAddtime(new Date());
        // 同一个业务标识
        companyMoneyRecordsBean.setTranId(System.currentTimeMillis());

        companyMoneyRecordsWriterMapper.insert(companyMoneyRecordsBean);
    }

    /**
     * 插入企业账户资金流水
     *
     * @param custId
     * @param moneyNow
     * @param amount       企业资金变动 正为收入 负为支出
     * @param recordType   记录类型 1收入 2支付
     * @param recordSource 记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+
     */
    private void insertCustomerMoneyRecords(Long custId, BigDecimal moneyNow, BigDecimal amount,
                                            Integer recordType, Integer recordSource, Long resourceId) {
        CustomerMoneyRecordsBean customerMoneyRecordsBean = new CustomerMoneyRecordsBean();
        //客户Id
        customerMoneyRecordsBean.setRecordCustomerId(custId);
        //客户资金变动 正为收入 负为支出
        customerMoneyRecordsBean.setRecordMoney(amount);
        //当前剩余
        customerMoneyRecordsBean.setRecordMoneyNow(moneyNow);
        //记录类型 1收入 2支付
        customerMoneyRecordsBean.setRecordType(recordType);
        //记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+8冻结 9退款
        customerMoneyRecordsBean.setRecordSource(recordSource);
        //记录产生来源Id
        customerMoneyRecordsBean.setResourceId(resourceId);
        //记录产生时间
        customerMoneyRecordsBean.setRecordAddtime(new Date());
        //1正常，0无效
        customerMoneyRecordsBean.setSign(1);

        customerMoneyRecordsWriterMapper.insert(customerMoneyRecordsBean);
    }

    /**
     * 根据custId查询账户详情
     *
     * @param custId
     * @param property 1-个人 2-企业
     * @return
     */
    public SubAccountBean selectByCustId(Long custId, int property) {
        if (null == custId) {
            return null;
        }
        SubAccountBean subAccountBean = new SubAccountBean();
        subAccountBean.setCustId(custId);
        subAccountBean.setProperty(property);
        return subAccountReaderMapper.selectSubAccountBean(subAccountBean);
    }

    /**
     * 更新账户余额自动转入控制
     *
     * @param custId
     * @param property
     * @param isAutoTransfer
     * @return
     */
    public int updateAutoTransfer(Long custId, int property, int isAutoTransfer) {
        return subAccountWriterMapper.updateAutoTransfer(custId, property, isAutoTransfer);
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sub_account
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(SubAccountBean record) {
        return subAccountWriterMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public void backPayoff(CompanyRechargesBean companyRechargesBean, String desc) {
        // 1. rechargeNumber + property 查询记录

        // 2. 更新充值提现表
        if(companyRechargesBean.getRechargeState() == 0) {
//            CompanyRechargesBean updateBean = new CompanyRechargesBean();
//            updateBean.setRechargeId(companyRechargesBean);

        }
        // 3. 插入退款流水

    }

    @Override
    public void backPayoff(CustomerRechargesBean customerRechargesBean, String desc) {
        // 1. rechargeNumber + property 查询记录

        // 2. 更新充值提现表
        if(customerRechargesBean.getRechargeState() == 0) {


        }
        // 3. 插入退款流水

    }

    /**
     * 批量插入员工账户
     * @param list
     * @return
     */
    public int insertBatch(List<SubAccountBean> list){
        return subAccountWriterMapper.insertBatch(list);
    }


}
