package com.xtr.core.service.shebao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.ShebaoDepoitService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.sbt.SbtResponse;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.account.SubAccountReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.account.RedEnvelopeWriterMapper;
import com.xtr.core.persistence.writer.account.SubAccountWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyMoneyRecordsWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyRechargesWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerMoneyRecordsWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import com.xtr.core.service.account.SubAccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by allycw3 on 2016/10/13.
 */
@Service("shebaoDepoitService")
public class ShebaoDepoitServiceImpl implements ShebaoDepoitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShebaoDepoitServiceImpl.class);

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

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Resource
    private CustomerShebaoWriterMapper customerShebaoWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    /**
     * 社保公积金扣款
     * @param companyRechargeId
     * @return
     */
    @Transactional
    public ResultResponse shebaoOrderDebit(Long companyRechargeId, String salaryOrderFlag, CompanyRechargesBean companyRechargesBean, CompanyRechargesBean checkBean)throws BusinessException,Exception {
        ResultResponse resultResponse = new ResultResponse();

        if(companyRechargeId==null){
            throw new BusinessException("社保公积金扣款,请传递订单参数");
        }
        //获取社保公积金订单信息
        CompanyRechargesBean rechargesBean=companyRechargesService.selectByPrimaryKey(companyRechargeId);
        //获取社保公积金详情列表
        if(rechargesBean==null && rechargesBean.getRechargeCompanyId()==null){
            throw new BusinessException("社保公积金扣款,无企业订单信息");
        }
        //获取企业社保公积金订单
        CompanyShebaoOrderBean orderBean=companyShebaoService.selectByPrimaryKey(rechargesBean.getExcelId());
        if(orderBean==null || orderBean.getOrderLastTime()==null){
            throw new BusinessException("社保公积金扣款,无社保公积金信息或无最后支付时间");
        }
        //验证付款时间不能超过最后支付时间
        Date orderLastTime=orderBean.getOrderLastTime();
        if(new Date().after(orderLastTime)){
            throw new BusinessException("超过社保公积金最后支付时间");
        }

        //检查企业账户协议状态
        checkShebaoProtocol(rechargesBean.getRechargeCompanyId());

        //获取工资发放总金额
        BigDecimal totalWages = orderBean.getPriceSum();
        totalWages = totalWages == null ? new BigDecimal("0") : totalWages;

        //获取最新的订单信息
        CompanyRechargesBean paymentBean = this.queryPaymentInfoForShebao(rechargesBean.getRechargeCompanyId(), totalWages);

        CompanyRechargesBean successCompanyRechargesBean = new CompanyRechargesBean();
        //订单ID
        successCompanyRechargesBean.setRechargeId(companyRechargeId);
        //订单交易成功
        successCompanyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_SUCCESS);
        //最后支付日期
        successCompanyRechargesBean.setRechargeRecallTime(new Date());

        //获取最新的红包金额信息
//        RedEnvelopeBean lastEnvelopeBean=assemRedWalletInfo(rechargesBean.getRechargeCompanyId(),paymentBean);
        if(paymentBean.getRechargeRedId()!=null){
            successCompanyRechargesBean.setRechargeRedId(paymentBean.getRechargeRedId());
        }

        //余额扣款前验证垫付金额\还需支付金额\可用余额是否有变动
        if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
            checkDifferentAmount(checkBean, paymentBean, rechargesBean.getRechargeCompanyId());
        }

        //更新企业社保公积金订单状态
        CompanyShebaoOrderBean sehbaoOrderBean=new CompanyShebaoOrderBean();
        sehbaoOrderBean.setId(orderBean.getId());
        //状态 0初始化，1待提交 2待付款 3付款中 4客户付款成功 5办理中 6办理完结 7订单关闭
        sehbaoOrderBean.setStatus(ShebaoConstants.COMPANY_ORDER_PAYSUCCESS);
        int result=companyShebaoOrderWriterMapper.updateByPrimaryKeySelective(sehbaoOrderBean);
        if (result > 0) {
            //扣款
            BigDecimal totalAmount=new BigDecimal("0");
            if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
                totalAmount=paymentBean.getRechargeRealAmount();
            } else if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE.equals(salaryOrderFlag)) {//充值的话,实际支付金额取充值时的金额,不取最新的数据
                totalAmount=companyRechargesBean.getRechargeRealAmount();
            }
            if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
                if(totalAmount.compareTo(new BigDecimal("0"))!=0){//余额扣款,如果扣的金额为0,则不扣款
                    subAccountService.deduct(rechargesBean.getRechargeCompanyId(), totalAmount, CompanyRechargeConstant.SOCIAL_TYPE, successCompanyRechargesBean.getRechargeId(), AccountType.COMPANY, paymentBean.getRechargePaymentAmount());
                }
            } else if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE.equals(salaryOrderFlag)) {
                subAccountService.deduct(rechargesBean.getRechargeCompanyId(), totalAmount, CompanyRechargeConstant.SOCIAL_TYPE, successCompanyRechargesBean.getRechargeId(), AccountType.COMPANY, companyRechargesBean.getRechargePaymentAmount());
            }
            //更新订单最新信息
            if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
                successCompanyRechargesBean.setRechargeStation(CompanyRechargeConstant.COMPANY_RECHARGE_STATION_YOUE);
                //垫付比例和垫付金额
                successCompanyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
                successCompanyRechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
                //实际支付额度
                successCompanyRechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
                int count = companyRechargesService.updateByPrimaryKeySelective(successCompanyRechargesBean);
                if (count <= 0) {
                    throw new BusinessException("充值发工资订单从账户余额扣款,更新发工资订单失败");
                }
            } else if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE.equals(salaryOrderFlag)) {
                successCompanyRechargesBean.setRechargeSerialNumber(companyRechargesBean.getRechargeSerialNumber());
                successCompanyRechargesBean.setRechargeBak(companyRechargesBean.getRechargeBak());
                successCompanyRechargesBean.setRechargeStation(CompanyRechargeConstant.COMPANY_RECHARGE_STATION_HUIKUAN);
                successCompanyRechargesBean.setRechargeBank(companyRechargesBean.getRechargeBank());
                successCompanyRechargesBean.setRechargeBanknumber(companyRechargesBean.getRechargeBanknumber());
                //垫付比例和垫付金额
                successCompanyRechargesBean.setRechargePaymentRate(companyRechargesBean.getRechargePaymentRate());
                successCompanyRechargesBean.setRechargePaymentAmount(companyRechargesBean.getRechargePaymentAmount());
                //实际支付额度
                successCompanyRechargesBean.setRechargeRealAmount(companyRechargesBean.getRechargeRealAmount());
                int count = companyRechargesService.updateByPrimaryKeySelective(successCompanyRechargesBean);
                if (count <= 0) {
                    throw new BusinessException("充值发工资订单审核,更新发工资订单失败");
                }
            }
            //更新红包状态为已使用状态
            RedEnvelopeBean redEnvelopeBean=new RedEnvelopeBean();
            if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
                if(paymentBean.getRechargeRedId()!=null)
                    redEnvelopeBean.setRedId(paymentBean.getRechargeRedId());
            }else if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE.equals(salaryOrderFlag)) {
                redEnvelopeBean.setRedId(companyRechargesBean.getRechargeRedId());
            }
            //红包状态 0:取消 1:未使用 2:已使用
            redEnvelopeBean.setState(2);
            redEnvelopeWriterMapper.updateByPrimaryKeySelective(redEnvelopeBean);
            //个人补退账务(由定时任务完成)

            //更改社保通订单的状态
            CustomerShebaoOrderBean updateStateBean=new CustomerShebaoOrderBean();
            updateStateBean.setCompanyShebaoOrderId(orderBean.getId());
            updateStateBean.setSbtStatus(ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_SUCCESS);
            customerShebaoOrderService.updateSbtStateForPayment(updateStateBean);

            //更新员工工资单状态
            companyProtocolsService.updateSalaryState(rechargesBean.getRechargeCompanyId(),CompanyProtocolConstants.PROTOCOL_TYPE_DJ ,0);

            //更改社保通接口的状态为已通过
            SheBaoTong sheBaoTong = new SheBaoTong(true);
            SbtResponse passResponseBefore=sheBaoTong.orderManager(orderBean.getShebaotongServiceNumber(), 1);
            JSONObject passResBefore = JSON.parseObject(passResponseBefore.getData());
            LOGGER.info("更改社保通状态为通过返回结果:"+JSON.toJSONString(passResBefore));
            if(StringUtils.isStrNull(passResBefore.getString("result")) || (!StringUtils.isStrNull(passResBefore.getString("result")) && !"1".equals(passResBefore.getString("result")))){
                throw new BusinessException("更改社保通状态为通过失败");
            }

            //更新社保通状态
            long starteTime=System.currentTimeMillis();
            updateShebaoCustomerState(orderBean.getId());
            LOGGER.info("更新社保通状态执行时间:"+(System.currentTimeMillis()-starteTime));

            resultResponse.setSuccess(true);
        } else {
            throw new BusinessException("发工资失败，请刷新界面重试");
        }
        return resultResponse;
    }

    /**
     * 更新社保通状态
     * @param companyOrderId
     * @throws BusinessException
     * @throws Exception
     */
    private void updateShebaoCustomerState(long companyOrderId)throws BusinessException,Exception{
//        List<CustomerShebaoOrderBean> shebaoOrderList=customerShebaoOrderService.selectByCompanyOrderId( companyOrderId);
//        if(shebaoOrderList!=null && shebaoOrderList.size()>0){
//            final List<Exception> errorList = new ArrayList();
//            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
//            int size = shebaoOrderList.size();
//            ExecutorService pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
//            for (final CustomerShebaoOrderBean assemOrderBean : shebaoOrderList) {
//                rowResult.add(pool.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            CustomerShebaoBean record=new CustomerShebaoBean();
//                            record.setCustomerId(assemOrderBean.getCustomerId());
//                            record.setCurrentCompanyOrderId(assemOrderBean.getCompanyShebaoOrderId());
//                            if(assemOrderBean.getSbtStatus()!=null && (assemOrderBean.getSbtStatus()==ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_SUCCESS || assemOrderBean.getSbtStatus()==ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_INIT )){//更改成功员工的状态
//                                if(assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.ZY
//                                        ||assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.HJ
//                                        ||assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ){
//                                    if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
//                                        record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_ING);
//                                    }else if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                                        record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_ING);
//                                    }
//                                }else if(assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.STOP){
//                                    if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
//                                        record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_STOP);
//                                    }else if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                                        record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_STOP);
//                                    }
//                                }
//                            }else if(assemOrderBean.getSbtStatus()!=null && assemOrderBean.getSbtStatus()==ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_FAIL){//更改失败员工的状态
//                                if(assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.ZY
//                                        ||assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.HJ
//                                        ||assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ
//                                        ||assemOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.STOP){
//                                    if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
//                                        record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_FAIL);
//                                    }else if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                                        record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_FAIL);
//                                    }
//                                }
//                            }
//                            if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO && assemOrderBean.getOrderType()!=null && assemOrderBean.getOrderType().intValue()!=ShebaoConstants.OrderType.BJ){
//                                customerShebaoWriterMapper.updateSbAndGjjStateByCondition(1,record);
//                            }else if(assemOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING && assemOrderBean.getOrderType()!=null && assemOrderBean.getOrderType().intValue()!=ShebaoConstants.OrderType.BJ){
//                                customerShebaoWriterMapper.updateSbAndGjjStateByCondition(2,record);
//                            }
//                        } catch (Exception e) {
//                            //添加一次信息
//                            errorList.add(e);
//                            //输出异常信息
//                            LOGGER.error("ID【" + assemOrderBean.getId() + "】出现异常···" + e.getMessage(), e);
//                        }
//                    }
//                }));
//
//            }
//            //等待处理结果
//            for (Future f : rowResult) {
//                f.get();
//            }
//            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
//            pool.shutdown();
//            //出现异常
//            if (!errorList.isEmpty()) {
//                //抛出异常信息
//                throw new BusinessException(errorList.get(0).getMessage());
//            }
//        }

        List<CustomerShebaoOrderBean> shebaoOrderList=customerShebaoOrderService.selectByCompanyOrderId( companyOrderId);
        List<CustomerShebaoBean> assemList=new ArrayList<>();
        if(shebaoOrderList!=null && shebaoOrderList.size()>0) {
            for (CustomerShebaoOrderBean assemOrderBean : shebaoOrderList) {
                CustomerShebaoBean record = new CustomerShebaoBean();
                record.setCustomerId(assemOrderBean.getCustomerId());
                record.setCurrentCompanyOrderId(assemOrderBean.getCompanyShebaoOrderId());
                if (assemOrderBean.getSbtStatus() != null && (assemOrderBean.getSbtStatus() == ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_SUCCESS || assemOrderBean.getSbtStatus() == ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_INIT)) {//更改成功员工的状态
                    if (assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.ZY
                            || assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.HJ
                            || assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.TJ) {
                        if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_SHEBAO) {
                            record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_ING);
                        } else if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING) {
                            record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_ING);
                        }
                    } else if (assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.STOP) {
                        if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_SHEBAO) {
                            record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_STOP);
                        } else if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING) {
                            record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_STOP);
                        }
                    }
                } else if (assemOrderBean.getSbtStatus() != null && (assemOrderBean.getSbtStatus() == ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_FAIL || assemOrderBean.getSbtStatus() == ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_SUBMITEXCEPTION)) {//更改失败员工的状态
                    if (assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.ZY
                            || assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.HJ
                            || assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.TJ
                            || assemOrderBean.getOrderType().intValue() == ShebaoConstants.OrderType.STOP) {
                        if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_SHEBAO) {
                            record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_FAIL);
                        } else if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING) {
                            record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_FAIL);
                        }
                    }
                }
                if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_SHEBAO && assemOrderBean.getOrderType() != null && assemOrderBean.getOrderType().intValue() != ShebaoConstants.OrderType.BJ) {
                    record.setIsType(1);
                    assemList.add(record);
                } else if (assemOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING && assemOrderBean.getOrderType() != null && assemOrderBean.getOrderType().intValue() != ShebaoConstants.OrderType.BJ) {
                    record.setIsType(2);
                    assemList.add(record);
                }

            }
            if(assemList!=null && assemList.size()>0){
                customerShebaoWriterMapper.updateSbAndGjjStateByConditionForBatch(assemList);
            }
        }
    }
    public void opeationBackDetail(){
        //根据企业社保公积金ID获取企业下的所有的个人补退的信息

        //先充值

        //再提交
    }
    /**
     * 检查企业账户协议状态
     *
     * @param companyId
     */
    public void checkShebaoProtocol(Long companyId) {
        ////签约类型：1代发协议 2垫发协议 3社保代缴协议',
        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectIsUserFulByTypeAndTime(companyId, CompanyProtocolConstants.PROTOCOL_TYPE_DJ, DateUtil.getCurrDateOfDate(DateUtil.dateString));
        if (companyProtocolsBean != null) {
            //协议状态:1待审批 2签约 3即将到期 4合约到期 5冻结
            if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 1) {
                throw new BusinessException("社保公积金代缴协议还未审批，不能进行社保公积金代缴业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 4) {
                throw new BusinessException("社保公积金代缴协议合约已到期，不能进行社保公积金代缴业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 5) {
                throw new BusinessException("社保公积金代缴协议合约已被冻结，不能进行社保公积金代缴业务");
            }
        } else {
            throw new BusinessException("未签署社保公积金代缴协议，不能进行社保公积金代缴业务");
        }
    }

    /**
     * 扣款前验证垫付金额\还需支付金额\可用余额是否有变动
     *
     * @param checkBean
     * @param paymentBean
     * @param companyId
     */
    public void checkDifferentAmount(CompanyRechargesBean checkBean, CompanyRechargesBean paymentBean, Long companyId) {
        //扣款前验证垫付金额\还需支付金额\可用余额是否有变动
        if (checkBean.getRechargePaymentAmount().compareTo(paymentBean.getRechargePaymentAmount()) != 0) {//垫付金额
            throw new BusinessException("垫付金额有变动,请重新打开详情页面查看最新垫付金额");
        }
        //获取最新的可用余额
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyId, AccountType.COMPANY);
        BigDecimal paymentAmount = (subAccountBean != null && subAccountBean.getCashAmout() != null) ? subAccountBean.getCashAmout() : new BigDecimal("0");
        if (paymentAmount.compareTo(checkBean.getCanUserCashAmount()) != 0) {//可用余额
            throw new BusinessException("可用余额有变动,请重新打开详情页面查看最新可用余额");
        }
        if (checkBean.getRedWalletAmount().compareTo(paymentBean.getRedWalletAmount()) != 0) {//实际支付金额
            throw new BusinessException("红包金额有变动,请重新打开详情页面查看最新红包金额");
        }
        if (checkBean.getRechargeRealAmount().compareTo(paymentBean.getRechargeRealAmount()) != 0) {//实际支付金额
            throw new BusinessException("实际支付金额有变动,请重新打开详情页面查看最新实际支付金额");
        }

    }

    /**
     * 获取红包金额信息
     * @param comapnyId
     * @param paymentBean
     */
    public RedEnvelopeBean assemRedWalletInfo(Long comapnyId,CompanyRechargesBean paymentBean){
        // 红包
        RedEnvelopeBean redEnvelopeBean = redEnvelopeService.getByCompanyId(comapnyId, CompanyRechargeConstant.SOCIAL_TYPE);
        BigDecimal redMoney=new BigDecimal("0");
        if(redEnvelopeBean!=null && redEnvelopeBean.getRedMoney()!=null){
            redMoney=redEnvelopeBean.getRedMoney();
        }
        paymentBean.setRedWalletAmount(redMoney);
        return redEnvelopeBean;
    }

    /**
     * 获取最新的垫付金额\垫付比例及红包
     *
     * @param companyId
     * @param totalWages
     * @return
     */
    public CompanyRechargesBean queryPaymentInfoForShebao(Long companyId, BigDecimal totalWages) {
        BigDecimal zeroBigDeciaml = new BigDecimal("0");
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        // 红包
        RedEnvelopeBean redEnvelopeBean = redEnvelopeService.getByCompanyId(companyId, CompanyRechargeConstant.SOCIAL_TYPE);
        BigDecimal redMoney=new BigDecimal("0");
        if(redEnvelopeBean!=null && redEnvelopeBean.getRedMoney()!=null){
            redMoney=redEnvelopeBean.getRedMoney();
        }
        companyRechargesBean.setRedWalletAmount(redMoney);
        if(redEnvelopeBean!=null){//红包ID
            companyRechargesBean.setRechargeRedId(redEnvelopeBean.getRedId());
        }
        //要付款的总金额减去红包的金额
        BigDecimal aviAmount=totalWages.subtract(companyRechargesBean.getRedWalletAmount());
        if(aviAmount.compareTo(zeroBigDeciaml)<=0){//如果红包已经把总金额全包括了
            companyRechargesBean.setRechargePaymentRate(zeroBigDeciaml);
            companyRechargesBean.setRechargePaymentAmount(zeroBigDeciaml);
            companyRechargesBean.setRechargeRealAmount(zeroBigDeciaml);
        }else{
            //垫付金额,垫付比例
            CompanyProtocolsBean companyProtocolsBean = new CompanyProtocolsBean();
            companyProtocolsBean.setProtocolCompanyId(companyId);
            companyProtocolsBean.setProtocolContractType(CompanyProtocolConstants.PROTOCOL_TYPE_FF);
            List<CompanyProtocolsBean> protocolList = companyProtocolsService.selectByContractType(companyProtocolsBean);
            if (protocolList != null && protocolList.size() > 0) {
                for (CompanyProtocolsBean protocolsBean : protocolList) {
                    if (protocolsBean != null && protocolsBean.getProtocolCurrentStatus() != null
                            && (protocolsBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
                            || protocolsBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL)) {
                        BigDecimal paymentRate = protocolsBean.getProtocolScale() == null ? zeroBigDeciaml : protocolsBean.getProtocolScale();
                        companyRechargesBean.setRechargePaymentRate(paymentRate);
                        //这儿的比例取的是总金额减去红包的金额
                        BigDecimal rechargePaymentAmount = paymentRate.multiply(aviAmount).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
                        companyRechargesBean.setRechargePaymentAmount(rechargePaymentAmount);
                        break;
                    }
                }
            }
            if (companyRechargesBean.getRechargePaymentAmount() == null || companyRechargesBean.getRechargePaymentRate() == null) {
                companyRechargesBean.setRechargePaymentRate(zeroBigDeciaml);
                companyRechargesBean.setRechargePaymentAmount(zeroBigDeciaml);
            }
            //判断是否超过垫付额度，如果超过，则取垫付额度，如果没超过，取垫付全额
            SubAccountBean subAccountBean = subAccountWriterMapper.selectByCustId(companyId, 2);
            if (subAccountBean != null && subAccountBean.getUncashAmount() != null) {
                BigDecimal uncashAmount = subAccountBean.getUncashAmount() != null ? subAccountBean.getUncashAmount() : new BigDecimal("0");
                if (uncashAmount.compareTo(companyRechargesBean.getRechargePaymentAmount()) < 0) {
                    companyRechargesBean.setRechargePaymentAmount(uncashAmount);
                }
            }
            LOGGER.info("获取最新的扣款数据,垫付比例:" + companyRechargesBean.getRechargePaymentRate() + ",垫付金额:" + companyRechargesBean.getRechargePaymentAmount()+",红包:"+companyRechargesBean.getRedWalletAmount());

            //实际支付额度
            companyRechargesBean.setRechargeRealAmount(aviAmount.subtract(companyRechargesBean.getRechargePaymentAmount()));
        }
        return companyRechargesBean;
    }

    /**
     * 生成充值订单
     *
     * @param companyRechargesBean
     * @return
     */
    public void generateCzOrderForShebao(CompanyRechargesBean companyRechargesBean) {

        if (null == companyRechargesBean) {
            throw new BusinessException("企业充值提现参数为空");
        }

        LOGGER.info("社保公积金充值订单生成,接受参数：" + JSON.toJSONString(companyRechargesBean));

        // 非空验证
        if(org.apache.commons.lang3.StringUtils.isBlank(companyRechargesBean.getRechargeBank()) ||
                org.apache.commons.lang3.StringUtils.isBlank(companyRechargesBean.getRechargeBanknumber()) ||
                org.apache.commons.lang3.StringUtils.isBlank(companyRechargesBean.getRechargeSerialNumber()) ||
                org.apache.commons.lang3.StringUtils.isBlank(companyRechargesBean.getRechargeBak()) ||
                null == companyRechargesBean.getRechargeMoney()){
            throw new BusinessException("请确认信息填写完整");
        }

        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyRechargesBean.getRechargeCompanyId(), AccountType.COMPANY);
        if (null == subAccountBean) {
            throw new BusinessException("企业账户未开通");
        }

        // 验证金额
        if (companyRechargesBean.getRechargeMoney().compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException("请输入正确的充值金额");
        }

        // 记录添加时间
        companyRechargesBean.setRechargeAddtime(new Date());
        // 审核状态
        companyRechargesBean.setRechargeState(0);
        // 是否体现到账户资金变化
        companyRechargesBean.setRechargeIstorecord(0);
        // 是否已经导出
        companyRechargesBean.setRechargeIstoexcel(0);
        // 生成流水号
        companyRechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.COMPANY_RECHARGE));

        companyRechargesBean.setRechargeMoneynow(subAccountBean.getAmout());
        int count=companyRechargesWriterMapper.insert(companyRechargesBean);
        if(count<=0){
            throw new BusinessException("社保公积金充值订单生成失败");
        }
    }

    /**
     * 社保公积金充值订单生成
     * @param companyRechargesBean
     * @param companyId
     */
    public void shebaoRecharge(CompanyRechargesBean companyRechargesBean,Long companyId){
        if(companyRechargesBean.getRechargeRealAmount()==null){
            throw new BusinessException("无法付款,还需支付不能为空,请联系管理员");
        }
        if(companyRechargesBean.getRechargeRealAmount().compareTo(new BigDecimal("0"))==0){
            throw new BusinessException("还需支付0元,请选择账户余额支付");
        }

        //检查企业账户协议状态
        this.checkShebaoProtocol(companyId);

        //获取企业社保公积金订单
        CompanyShebaoOrderBean orderBean=companyShebaoService.selectByPrimaryKey(companyRechargesBean.getExcelId());
        if(orderBean==null || orderBean.getOrderLastTime()==null){
            throw new BusinessException("社保公积金扣款,无社保公积金信息或无最后支付时间");
        }
        //验证付款时间不能超过最后支付时间
        Date orderLastTime=orderBean.getOrderLastTime();
        if(new Date().after(orderLastTime)){
            throw new BusinessException("超过社保公积金最后支付时间");
        }

        //获取工资发放总金额
        BigDecimal totalWages = orderBean.getPriceSum();
        totalWages = totalWages == null ? new BigDecimal("0") : totalWages;
        CompanyRechargesBean paymentBean = this.queryPaymentInfoForShebao(companyId, totalWages);

        //充值前验证垫付金额\还需支付金额\红包\可用余额是否有变动
        checkDifferentAmount(companyRechargesBean, paymentBean, companyId);

        //订单关联的企业ID
        companyRechargesBean.setRechargeCompanyId(companyId);
        //订单类型
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.RECHARGE_TYPE);
        //订单支付渠道
        companyRechargesBean.setRechargeStation(CompanyRechargeConstant.COMPANY_RECHARGE_STATION_HUIKUAN);
        //订单名称
        String name = new SimpleDateFormat("yyyy年MM月").format(new Date()) + CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_SHEBAORECHARGE;
        companyRechargesBean.setRechargeName(name);
        //垫付比例
        companyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
        companyRechargesBean.setRechargeRedId(paymentBean.getRechargeRedId());
        companyRechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
        companyRechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
        companyRechargesBean.setRechargeMoney(paymentBean.getRechargeRealAmount());
        this.generateCzOrderForShebao(companyRechargesBean);

        //更新企业社保公积金状态为付款中
        CompanyShebaoOrderBean updateOrderBean=new CompanyShebaoOrderBean();
        updateOrderBean.setId(orderBean.getId());
        updateOrderBean.setStatus(ShebaoConstants.COMPANY_ORDER_PAYING);
        int count=companyShebaoService.updateByPrimaryKeySelective(updateOrderBean);
        if(count<=0){
            throw new BusinessException("更新状态为付款中失败");
        }
    }

}
