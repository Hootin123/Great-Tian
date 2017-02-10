package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.QueryParam;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.RedReceiveBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.*;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.account.RedReceiveService;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.*;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.ShebaoDepoitService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.*;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.comm.sbt.SbtResponse;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.company.CompanyBorrowOrdersReaderMapper;
import com.xtr.core.persistence.reader.company.CompanyRechargesReaderMapper;
import com.xtr.core.persistence.reader.company.CompanySocialOrdersReaderMapper;
import com.xtr.core.persistence.writer.account.SubAccountWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyRechargesWriterMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.company.CompanySocialOrdersWriterMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>企业充值提现</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 11:51
 */
@Service("companyRechargesService")
public class CompanyRechargesServiceImpl implements CompanyRechargesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRechargesServiceImpl.class);

    @Resource
    private CompanyRechargesReaderMapper companyRechargesReaderMapper;

    @Resource
    private CompanyRechargesWriterMapper companyRechargesWriterMapper;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanySocialOrdersWriterMapper companySocialOrdersWriterMapper;

    @Resource
    private CompanySocialOrdersReaderMapper companySocialOrdersReaderMapper;

    @Resource
    private CompanysService companysService;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    @Resource
    private RedReceiveService redReceiveService;

    @Resource
    private CompanyMsgsService companyMsgsService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private CompanyBorrowOrdersReaderMapper companyBorrowOrdersReaderMapper;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CompanyShebaoOrderWriterMapper companyShebaoOrderWriterMapper;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private ShebaoDepoitService shebaoDepoitService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private SubAccountWriterMapper subAccountWriterMapper;

    /**
     * 根据主键查询数据库记录
     *
     * @param rechargeId
     */
    public CompanyRechargesBean selectByPrimaryKey(Long rechargeId) {
        return companyRechargesReaderMapper.selectByPrimaryKey(rechargeId);
    }

    /**
     * 分页查询订单
     *
     * @param comapnyId
     * @param rechargeType
     * @param rechargeState
     * @param dateType      0:全部 1:一周内 2:一个月内 3:三个月内
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultResponse selectPageList(Long comapnyId, Integer rechargeType, Integer rechargeState, int dateType, int pageIndex, int pageSize) {

        ResultResponse resultResponse = new ResultResponse();

        if (null == comapnyId) {
            return resultResponse;
        }

        Date start = null;
        Date end = null;

        QueryParam qp = new QueryParam();

        if (dateType == 1) {
            end = new Date();
            start = DateUtil.addDate(end, -7);
        }

        if (dateType == 2) {
            end = new Date();
            start = DateUtil.addDateOfMonth(end, -1);
        }

        if (dateType == 3) {
            end = new Date();
            start = DateUtil.addDateOfMonth(end, -3);
        }

        if (null != rechargeType) {
            qp.eq("recharge_type", rechargeType);
        } else {
            qp.in("recharge_type", 1, 2, 3, 8, 5, 4, 9);
        }

        qp.eq("recharge_company_id", comapnyId);
        qp.eq("recharge_state", rechargeState);
        qp.gte("recharge_addtime", start);
        qp.lte("recharge_addtime", end);

        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);

        PageList<CompanyRechargesBean> list = companyRechargesReaderMapper.selectPageList(qp, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);

        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));

        return resultResponse;
    }

    /**
     * 企业充值
     *
     * @param companyRechargesBean
     * @return
     * @throws BusinessException
     */
    @Transactional
    public CompanyRechargesBean addRecharge(CompanyRechargesBean companyRechargesBean) throws BusinessException {
        if (null == companyRechargesBean) {
            throw new BusinessException("企业充值提现参数为空");
        }

        LOGGER.info("接受参数：" + JSON.toJSONString(companyRechargesBean));

        // 非空验证
        if (StringUtils.isBlank(companyRechargesBean.getRechargeBank()) ||
                StringUtils.isBlank(companyRechargesBean.getRechargeBanknumber()) ||
                StringUtils.isBlank(companyRechargesBean.getRechargeSerialNumber()) ||
                StringUtils.isBlank(companyRechargesBean.getRechargeBak()) ||
                null == companyRechargesBean.getRechargeMoney()) {
            throw new BusinessException("请确认信息填写完整");
        }

        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyRechargesBean.getRechargeCompanyId(), AccountType.COMPANY);
        if (null == subAccountBean) {
            throw new BusinessException("企业账户未开通");
        }

        // 验证银行卡号
//        if(!RegexUtil.checkBankNumber(companyRechargesBean.getRechargeBanknumber())){
//            throw new BusinessException("请输入正确的银行卡号");
//        }

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

        try {
            companyRechargesBean.setRechargeMoneynow(subAccountBean.getAmout());
            companyRechargesWriterMapper.insert(companyRechargesBean);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return companyRechargesBean;
    }

    public void addRechargeByBorrowOrder(long borrowOrderId) throws BusinessException {
        CompanyBorrowOrdersBean companyBorrowOrders = companyBorrowOrdersReaderMapper.selectByPrimaryKey(borrowOrderId);
        if (companyBorrowOrders == null) {
            throw new BusinessException("借款订单信息不存在");
        }
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyBorrowOrders.getOrderCompanyId(), AccountType.COMPANY);
        if (null == subAccountBean) {
            throw new BusinessException("企业账户未开通");
        }
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();

        companyRechargesBean.setRechargeMoney(companyBorrowOrders.getOrderMoney());
        // 记录添加时间
        companyRechargesBean.setRechargeAddtime(new Date());
        // 审核状态
        companyRechargesBean.setRechargeState(2);
        // 是否体现到账户资金变化
        companyRechargesBean.setRechargeIstorecord(0);
        // 是否已经导出
        companyRechargesBean.setRechargeIstoexcel(0);
        // 生成流水号
        String orderNumber = companyBorrowOrders.getOrderNumber();
        companyRechargesBean.setRechargeNumber(orderNumber);

        companyRechargesBean.setRechargeClient(1);
        companyRechargesBean.setRechargeStation(0);
        companyRechargesBean.setRechargeBak("放款");
        companyRechargesBean.setRechargeCompanyId(companyBorrowOrders.getOrderCompanyId());
        companyRechargesBean.setRechargeType(5);//放款
        companyRechargesBean.setRechargeAuditFirstTime(new Date());
        try {
            companyRechargesBean.setRechargeMoneynow(subAccountBean.getAmout());
            companyRechargesWriterMapper.insert(companyRechargesBean);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * 企业提现
     *
     * @param companyId
     * @param rechargeMoney
     * @param rechargeClient
     * @param rechargeBank
     * @param bankAccountName
     * @param rechargeBanknumber
     * @param bankSubbranch      @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CompanyRechargesBean companyWithdrawals(Long companyId, BigDecimal rechargeMoney, Integer rechargeClient, String rechargeBank, String bankAccountName, String rechargeBanknumber, String bankSubbranch) throws BusinessException {

        if (null == companyId) {
            throw new BusinessException("企业ID参数为空");
        }

        if (null == rechargeMoney) {
            throw new BusinessException("金额参数为空");
        }

        // 验证金额
        if (rechargeMoney.compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException("请输入正确的充值金额");
        }

        if (StringUtils.isBlank(rechargeBank) || StringUtils.isBlank(bankAccountName) || StringUtils.isBlank(rechargeBanknumber) || StringUtils.isBlank(bankSubbranch)) {
            throw new BusinessException("请确认信息填写完整");
        }

        // 验证银行卡号
//        if (!RegexUtil.checkBankNumber(rechargeBanknumber)) {
//            throw new BusinessException("请输入正确的银行卡号");
//        }

        CompanysBean companysBean = companysService.selectCompanyByCompanyId(companyId);
        if (null == companysBean) {
            throw new BusinessException("企业信息不存在");
        }

        // 查询余额，银行卡账户名，银行卡号
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyId, AccountType.COMPANY);
        if (null == subAccountBean) {
            throw new BusinessException("企业账户不存在");
        }

        // 验证提现金额是否大于可用余额
        if (subAccountBean.getCashAmout().compareTo(rechargeMoney) < 0) {
            throw new BusinessException("您的提现金额大于可用余额，请您重新填写");
        }

        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        companyRechargesBean.setRechargeCompanyId(companyId);
        companyRechargesBean.setRechargeClient(rechargeClient);
        companyRechargesBean.setRechargeMoney(rechargeMoney);

        // 查询银行名称和卡号
//        String companyDepositBankAccountName = companysBean.getCompanyDepositBankAccountName();
//        String companyDepositBankNo = companysBean.getCompanyDepositBankNo();

        companyRechargesBean.setRechargeBank(rechargeBank);
        companyRechargesBean.setRechargeBanknumber(rechargeBanknumber);
        companyRechargesBean.setBankSubbranch(bankSubbranch);
        companyRechargesBean.setBankAccountName(bankAccountName);

        // 提现类型
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.WITHDRAW_TYPE);
        // 记录添加时间
        companyRechargesBean.setRechargeAddtime(new Date());
        // 审核状态
        companyRechargesBean.setRechargeState(0);
        // 是否体现到账户资金变化
        companyRechargesBean.setRechargeIstorecord(0);
        // 是否已经导出
        companyRechargesBean.setRechargeIstoexcel(0);

        // 生成订单号
        String orderId = idGeneratorService.getOrderId(BusinessEnum.COMPANY_WITHDRAWALS);
        companyRechargesBean.setRechargeNumber(orderId);

        // 当前余额
        companyRechargesBean.setRechargeMoneynow(subAccountBean.getAmout());

        try {

            companyRechargesWriterMapper.insert(companyRechargesBean);

            /**
             * 冻结金额
             *
             * 减可用，加冻结
             */
            subAccountService.frozen(companyRechargesBean.getRechargeCompanyId(), companyRechargesBean.getRechargeMoney(),
                    CompanyRechargeConstant.WITHDRAW_TYPE, companyRechargesBean.getRechargeId(), AccountType.COMPANY);

        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return companyRechargesBean;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendSalary(BigDecimal totleCost, Long companyId, Long excelId) throws BusinessException {
        if (null == totleCost) {
            throw new BusinessException("发工资金额为空");
        }

        if (null == companyId) {
            throw new BusinessException("企业ID参数为空");
        }

        if (null == excelId) {
            throw new BusinessException("ExcelId为空");
        }

        // 查询余额，银行卡账户名，银行卡号
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyId, AccountType.COMPANY);
        if (null == subAccountBean) {
            throw new BusinessException("企业账户不存在");
        }

        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        companyRechargesBean.setRechargeCompanyId(companyId);
        companyRechargesBean.setRechargeClient(1);
        companyRechargesBean.setRechargeMoney(totleCost);
        companyRechargesBean.setExcelId(excelId);

        // 发工资类型
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.SEND_SALARY_TYPE);
        // 记录添加时间
        companyRechargesBean.setRechargeAddtime(new Date());
        // 审核状态
        companyRechargesBean.setRechargeState(0);
        // 是否体现到账户资金变化
        companyRechargesBean.setRechargeIstorecord(0);
        // 是否已经导出
        companyRechargesBean.setRechargeIstoexcel(0);

        // 生成订单号
        String orderId = idGeneratorService.getOrderId(BusinessEnum.SEND_SALARY);
        companyRechargesBean.setRechargeNumber(orderId);

        // 当前余额
        companyRechargesBean.setRechargeMoneynow(subAccountBean.getAmout());
        try {
            companyRechargesWriterMapper.insert(companyRechargesBean);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 分页查询企业流水
     *
     * @param companyRechargeDto
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultResponse selectRechargeList(CompanyRechargeDto companyRechargeDto, int pageIndex, int pageSize) {
        ResultResponse resultResponse = new ResultResponse();
        if (null != companyRechargeDto) {
            PageBounds pageBounds = new PageBounds(pageIndex, pageSize);
            PageList<CompanyRechargeDto> list = companyRechargesReaderMapper.selectPageRecords(companyRechargeDto, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = list.getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
            LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        }
        return resultResponse;
    }

    /**
     * 查询企业充值提现
     *
     * @param rechargeId
     * @return
     */
    public CompanyRechargeDto selectCompanyRechargeDto(Long rechargeId) {
        if (null != rechargeId) {
            return companyRechargesReaderMapper.selectCompanyRechargeDto(rechargeId);
        }
        return null;
    }

    /**
     * 审核充值
     * <p>
     * 1. 修改company_recharges审核状态以及审核时间
     * 2. 修改company_money_records审核状态
     * 3. 充值
     *
     * @param rechargeId
     * @param memberId
     * @param state
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultResponse auditRecharge(Long rechargeId, Long memberId, int state) throws BusinessException {
        ResultResponse resultResponse = new ResultResponse();
        if (null == rechargeId) {
            throw new BusinessException("充值提现ID不能为空");
        }

        if (null == memberId) {
            throw new BusinessException("审核人ID不能为空");
        }

        CompanyRechargesBean companyRechargesBean = this.selectByPrimaryKey(rechargeId);
        if (null == companyRechargesBean) {
            throw new BusinessException("不存在的资金变动记录");
        }

        try {

            // 避免重复审核
            if (companyRechargesBean.getRechargeState() == 0) {

                companyRechargesBean.setRechargeState(state);
                companyRechargesBean.setRechargeAuditFirstMember(memberId);
                companyRechargesBean.setRechargeAuditFirstTime(new Date());

                // 充值 审核通过，充值加款
                if (companyRechargesBean.getRechargeType() == 1 && state == 2) {
                    subAccountService.rechargeRecords(companyRechargesBean.getRechargeCompanyId(), companyRechargesBean.getRechargeMoney(), CompanyRechargeConstant.RECHARGE_TYPE, rechargeId, AccountType.COMPANY);
                    //如果是发工资充值,充完值直接扣款
                    if (!com.xtr.comm.util.StringUtils.isStrNull(companyRechargesBean.getRechargeName())
                            && (companyRechargesBean.getRechargeName().endsWith(CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_SALARYRECHARGE)
                            || companyRechargesBean.getRechargeName().endsWith(CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_RAPIDLYCHARGE))) {
                        if (companyRechargesBean.getExcelId() != null) {
                            //扣款
                            resultResponse = payrollAccountService.salaryOrderDebit(companyRechargesBean.getExcelId(), CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE, companyRechargesBean, companyRechargesBean);
                            if (!resultResponse.isSuccess()) {
                                throw new BusinessException(resultResponse.getMessage());
                            }
                        } else {
                            throw new BusinessException("充值发工资订单,没有计薪周期");
                        }
                    } else if (!com.xtr.comm.util.StringUtils.isStrNull(companyRechargesBean.getRechargeName())
                            && companyRechargesBean.getRechargeName().endsWith(CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_SHEBAORECHARGE)) {//如果是社保公积金充值,充完值直接扣款
                        if (companyRechargesBean.getExcelId() != null) {
//                            //更新企业社保公积金订单状态
//                            CompanyShebaoOrderBean sehbaoOrderBean=new CompanyShebaoOrderBean();
//                            sehbaoOrderBean.setId(companyRechargesBean.getExcelId());
//                            //状态 0初始化，1待提交 2待付款 3付款中 4客户付款成功 5办理中 6办理完结 7订单关闭
//                            sehbaoOrderBean.setStatus(ShebaoConstants.COMPANY_ORDER_PAYSUCCESS);
//                            int result=companyShebaoOrderWriterMapper.updateByPrimaryKeySelective(sehbaoOrderBean);
//                            if (result <= 0) {
//                                throw new BusinessException("审核社保公金充值订单,更改企业社保公积金状态为付款中失败");
//                            }
                            CompanyRechargesBean checkRechargeBean = new CompanyRechargesBean();
                            checkRechargeBean.setExcelId(companyRechargesBean.getExcelId());
                            checkRechargeBean.setRechargeType(CompanyRechargeConstant.SOCIAL_TYPE);
                            checkRechargeBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
                            checkRechargeBean.setRechargeCompanyId(companyRechargesBean.getRechargeCompanyId());
                            //获取社保公积金订单
                            List<CompanyRechargesBean> rechargeList = companyRechargesService.selectListByCondition(checkRechargeBean);
                            if (rechargeList != null && rechargeList.size() > 0) {
                                //扣款
                                resultResponse = shebaoDepoitService.shebaoOrderDebit(rechargeList.get(0).getRechargeId(), CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE, companyRechargesBean, null);
                                if (!resultResponse.isSuccess()) {
                                    throw new BusinessException(resultResponse.getMessage());
                                }
                            } else {
                                throw new BusinessException("审核社保公金充值订单,没有社保公积金的订单");
                            }

                        } else {
                            throw new BusinessException("审核社保公金充值订单,没有企业社保公积金订单");
                        }
                    }

                } else if (companyRechargesBean.getRechargeType() == 1 && state == 1) {//审核驳回
                    if (!com.xtr.comm.util.StringUtils.isStrNull(companyRechargesBean.getRechargeName())
                            && companyRechargesBean.getRechargeName().endsWith(CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_SHEBAORECHARGE)) {//如果是社保公积金充值,充完值直接扣款
                        if (companyRechargesBean.getExcelId() != null) {
                            CompanyShebaoOrderBean record = new CompanyShebaoOrderBean();
                            record.setId(companyRechargesBean.getExcelId());
                            record.setStatus(ShebaoConstants.COMPANY_ORDER_CLOSE);
                            companyShebaoService.updateByPrimaryKeySelective(record);
                            //更改社保通接口的状态为已通过
                            //获取企业社保公积金订单
                            CompanyShebaoOrderBean orderBean = companyShebaoService.selectByPrimaryKey(companyRechargesBean.getExcelId());
                            if (orderBean == null && com.xtr.comm.util.StringUtils.isStrNull(orderBean.getShebaotongServiceNumber())) {
                                throw new BusinessException("社保公积金付款,无社保公积金信息或无最后支付时间");
                            }
                            SheBaoTong sheBaoTong = new SheBaoTong(true);
                            SbtResponse cancelResponseBefore = sheBaoTong.orderManager(orderBean.getShebaotongServiceNumber(), 0);
                            JSONObject cancelResBefore = JSON.parseObject(cancelResponseBefore.getData());
                            LOGGER.info("更改社保通状态为取消返回结果:" + JSON.toJSONString(cancelResBefore));
//                            if(com.xtr.comm.util.StringUtils.isStrNull(cancelResBefore.getString("result")) || (!com.xtr.comm.util.StringUtils.isStrNull(cancelResBefore.getString("result")) && !"1".equals(cancelResBefore.getString("result")))){
//                                throw new BusinessException("更改社保通状态为取消失败");
//                            }
                        }

                    }
                }
                // 更新审核状态和时间
                companyRechargesWriterMapper.updateByPrimaryKey(companyRechargesBean);
            }

            resultResponse.setSuccess(true);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 提现审核
     *
     * @param rechargeId
     * @param memberId
     * @param rechargeSerialNumber
     * @param rechargeBak
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultResponse auditWithdrawals(Long rechargeId, Long memberId, String rechargeSerialNumber, String rechargeBak) throws BusinessException {
        ResultResponse resultResponse = new ResultResponse();
        if (null == rechargeId || null == memberId || StringUtils.isBlank(rechargeSerialNumber) ||
                StringUtils.isBlank(rechargeBak)) {
            throw new BusinessException("参数不能为空");
        }

        final CompanyRechargesBean companyRechargesBean = this.selectByPrimaryKey(rechargeId);
        if (null != companyRechargesBean) {

            // 避免重复审核
            if (companyRechargesBean.getRechargeState() == 0) {
                // 更新订单状态
                companyRechargesBean.setRechargeState(CompanyRechargeConstant.WITHDRAW_TYPE);
                companyRechargesBean.setRechargeSerialNumber(rechargeSerialNumber);
                companyRechargesBean.setRechargeBak(rechargeBak);
                companyRechargesBean.setRechargeAuditFirstMember(memberId);
                companyRechargesBean.setRechargeAuditFirstTime(new Date());
                companyRechargesWriterMapper.updateByPrimaryKey(companyRechargesBean);

                // 提现 更新扣款
                if (companyRechargesBean.getRechargeType() == CompanyRechargeConstant.WITHDRAW_TYPE) {
                    // 提现成功，解冻扣款
                    try {
                        subAccountService.thawDeduct(companyRechargesBean.getRechargeCompanyId(), companyRechargesBean.getRechargeMoney(),
                                CompanyRechargeConstant.WITHDRAW_TYPE, rechargeId, AccountType.COMPANY);


                        CompanysBean companysBean = companysService.selectCompanyByCompanyId(companyRechargesBean.getRechargeCompanyId());

                        if (null != companysBean) {
                            // 发送消息
                            CompanyMsgsBean companyMsgsBean = new CompanyMsgsBean();
                            String msgTitle = "提现申请处理成功提醒";
                            final String wh = com.xtr.comm.util.StringUtils.getHideCard(companyRechargesBean.getRechargeBanknumber(), 4);
                            String msgCont = "您好，您" + companyRechargesBean.getRechargeMoney() + "元提现至" + wh + "账户的申请已处理成功，请耐心等待银行处理。";
                            companyMsgsBean.setMsgTitle(msgTitle);//标题
                            companyMsgsBean.setMsgCont(msgCont);//消息内容
                            companyMsgsBean.setMsgType(2);// 2为消息提醒类型
                            companyMsgsBean.setMsgAddtime(new Date());//消息生成时间
                            companyMsgsBean.setMsgCompanyId(companyRechargesBean.getRechargeCompanyId());//企业id
                            companyMsgsBean.setMsgCompanyName(companysBean.getCompanyName());//企业名称
                            companyMsgsBean.setMsgFromCompanyId(0l);//long  类型
                            companyMsgsBean.setMsgSign(1);//状态  1：未读   2：已读  0：删除
                            companyMsgsBean.setMsgClass(1);//1：收件箱  2：发件箱
                            companyMsgsService.insert(companyMsgsBean);

                            // 异步发送短信
                            final CompanyMembersBean managerBean = companyMembersService.getCompanyManager(companyRechargesBean.getRechargeCompanyId());
                            if (null != managerBean) {
                                ExecutorService executorService = Executors.newCachedThreadPool();
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 发送短信
                                        String content = "您好，您" + companyRechargesBean.getRechargeMoney() + "元提现到账户" + wh + "的申请已处理成功，银行处理可能需要等待2-4个小时，请耐心等待。";
                                        try {
                                            sendMsgService.sendMsg(managerBean.getMemberPhone(), content);
                                            LOGGER.info("企业ID[{}],手机号码[{}],短信内容:[{}]", companyRechargesBean.getRechargeCompanyId(), managerBean.getMemberPhone(), content);
                                        } catch (IOException e) {
                                            LOGGER.error("短信发送失败", e);
                                        }
                                    }
                                });
                                executorService.shutdown();
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                        throw new BusinessException(e.getMessage());
                    }
                }
            }
            resultResponse.setSuccess(true);
        } else {
            throw new BusinessException("无效的提现订单");
        }
        return resultResponse;
    }

    /**
     * 社保订单银行支付
     *
     * @param companyRechargesBean
     * @param orderId
     * @param orderPaytype
     * @return
     */
    @Transactional
    @Override
    public int companySocialOrderBankPay(CompanyRechargesBean companyRechargesBean, long orderId, int orderPaytype) throws BusinessException {
        int result = 0;
        try {

            // 查询有没有代缴社保的红包
            RedEnvelopeBean redEnvelopeBean = redEnvelopeService.getByCompanyId(companyRechargesBean.getRechargeCompanyId(), CompanyRechargeConstant.SOCIAL_TYPE);

            if (null != redEnvelopeBean) {

                boolean used = false;

                CompanySocialOrdersBean ordersBean = companySocialOrdersReaderMapper.find(orderId);
                if (null == ordersBean) {
                    throw new BusinessException("不存在的订单");
                }

                Integer count = ordersBean.getOrderPeopleNumber();

                if (redEnvelopeBean.getActivityId().equals(RedActivityConstant.AC_22) && count >= 5) {
                    used = true;
                }

                if (redEnvelopeBean.getActivityId().equals(RedActivityConstant.AC_66) && count >= 15) {
                    used = true;
                }

                if (used) {
                    BigDecimal money = companyRechargesBean.getRechargeMoney();
                    BigDecimal realMoney = money.subtract(redEnvelopeBean.getRedMoney());
                    companyRechargesBean.setRechargeMoney(realMoney);

                    RedReceiveBean redReceiveBean = new RedReceiveBean();
                    redReceiveBean.setRedId(redEnvelopeBean.getRedId());
                    redReceiveBean.setOrderId(orderId);
                    redReceiveBean.setCompanyId(companyRechargesBean.getRechargeCompanyId());
                    redReceiveBean.setReceiveMoney(redEnvelopeBean.getRedMoney());
                    redReceiveBean.setResourceType(CompanyRechargeConstant.SOCIAL_TYPE);
                    redReceiveService.useRedEnvelope(redReceiveBean);
                }
            }

            companyRechargesWriterMapper.insertSelective(companyRechargesBean);

            long rechareId = companyRechargesBean.getRechargeId();

            CompanySocialOrdersBean socialOrdersBean = new CompanySocialOrdersBean();
            socialOrdersBean.setOrderId(orderId);
            socialOrdersBean.setOrderRechargeId(rechareId);
            socialOrdersBean.setOrderStatus(6);

            companySocialOrdersWriterMapper.updateByPrimaryKeySelective(socialOrdersBean);

        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        result = 1;
        return result;
    }

    /**
     * 根据企业id删除数据库记录
     *
     * @param companyId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteByCompanyId(Long companyId) throws BusinessException {
        if (null == companyId) {
            throw new BusinessException("企业id不能为空");
        }

        return companyRechargesWriterMapper.deleteByComapnyId(companyId);
    }

    /**
     * 更新企业充值提现状态
     *
     * @param companyId
     * @param excelId
     * @param state
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateRechargeState(Long companyId, Long excelId, Integer state) throws BusinessException {
        if (null == companyId) {
            throw new BusinessException("企业id不能为空");
        }
        if (null == excelId) {
            throw new BusinessException("ExcelId不能为空");
        }

        CompanyRechargesBean temp = new CompanyRechargesBean();
        temp.setRechargeCompanyId(companyId);
        temp.setExcelId(excelId);

        CompanyRechargesBean upObj = companyRechargesReaderMapper.selectCompanyRecharge(temp);
        if (null != upObj) {
            temp.setRechargeId(upObj.getRechargeId());
            temp.setRechargeState(state);

            companyRechargesWriterMapper.updateByPrimaryKeySelective(temp);
        } else {
            throw new BusinessException("不存在的工资单，企业id【" + companyId + "】，excelId【" + excelId + "】");
        }
    }

    @Override
    @SystemServiceLog(operation = "处理京东回调", modelName = "企业提现")
    public void doJdResponse(NotifyResponse response) {
        LOGGER.info("京东回调：充值处理----" + response);
        if (response == null || response.getBusinessType() != BusinessType.COMPANY_WITHDRAW) {
            return;
        }
        CompanyRechargesBean rechargesBean = companyRechargesReaderMapper.selectByNumber(response.getOutTradeNo());
        String tradeStatus = response.getTradeStatus();

        LOGGER.info("tradeStatus:" + tradeStatus);
        rechargesBean.setRechargeRecallTime(new Date());
        if (CodeConst.TRADE_WPAR.equals(tradeStatus) || CodeConst.TRADE_BUID.equals(tradeStatus) || CodeConst.TRADE_ACSU.equals(tradeStatus)) {
            LOGGER.info("订单等待中，暂不处理");
        } else if (CodeConst.TRADE_FINI.equals(tradeStatus)) {//交易成功
            LOGGER.info("订单交易成功");
            // 避免重复审核
            if (rechargesBean.getRechargeState() == 0 && rechargesBean.getRechargeRecallResult() == null) {
                // 更新订单状态
                rechargesBean.setRechargeRecallResult(1);
                rechargesBean.setRechargeState(CompanyRechargeConstant.WITHDRAW_TYPE);
                companyRechargesWriterMapper.updateByPrimaryKey(rechargesBean);
                subAccountService.thawDeduct(rechargesBean.getRechargeCompanyId(), rechargesBean.getRechargeMoney(),
                        CompanyRechargeConstant.WITHDRAW_TYPE, rechargesBean.getRechargeId(), AccountType.COMPANY);
            }

        } else if (CodeConst.TRADE_REFUND.equals(tradeStatus)) {
            //成功后退款处理
            LOGGER.info("订单退款");
            if (rechargesBean.getRechargeRecallResult() == 1) {
                //退款处理  还原订单状态
                rechargesBean.setRechargeRecallResult(0);
                rechargesBean.setRechargeState(0);
                companyRechargesWriterMapper.updateByPrimaryKey(rechargesBean);
                //退款金额
                BigDecimal refundAmount = new BigDecimal(response.getRefundAmount());
                refundAmount = refundAmount.divide(new BigDecimal(100));
                //充值退款金额
                subAccountService.rechargeRecords(rechargesBean.getRechargeCompanyId(), refundAmount, 9, rechargesBean.getRechargeId(), 2);
                //冻结退款金额
                subAccountService.frozen(rechargesBean.getRechargeCompanyId(), refundAmount, 9, rechargesBean.getRechargeId(), 2);
            }

        } else {
            LOGGER.info("订单失败");
            String tradeRespcode = response.getTradeRespcode();
            LOGGER.info("tradeRespcode:" + tradeRespcode + "  message:" + response.getResponseMessage());
            if (CodeConst.isCustomerReasons(tradeRespcode)) {//余额不足等待下次支付
                LOGGER.info("支付信息有误，");
                //支付信息有误，退款处理
                if (rechargesBean.getRechargeState() == 0 && rechargesBean.getRechargeRecallResult() == null) {
                    // 更新订单状态
                    rechargesBean.setRechargeRecallResult(2);
                    rechargesBean.setRechargeState(1);//支付失败不再支付，解冻金额
                    rechargesBean.setRechargeAuditFirstRemark(response.getResponseMessage());
                    companyRechargesWriterMapper.updateByPrimaryKey(rechargesBean);

                    subAccountService.backwash(rechargesBean.getRechargeCompanyId(), rechargesBean.getRechargeMoney(), CustomerRechargeConstant.BACKWASH, rechargesBean.getRechargeId(), 2);
                }
            } else {
                //不做处理
            }
        }
    }

    /**
     * 插入企业订单
     *
     * @param companyRechargesBean
     * @return
     */
    public Long insertSelective(CompanyRechargesBean companyRechargesBean) {
        companyRechargesWriterMapper.insertSelective(companyRechargesBean);
        return companyRechargesBean.getRechargeId();
    }

    /**
     * 根据订薪周期查询是否有生成的订单
     *
     * @param companyRechargesBean
     * @return
     */
    public List<CompanyRechargesBean> selectByExcelIdAndType(CompanyRechargesBean companyRechargesBean) {
        return companyRechargesReaderMapper.selectByExcelIdAndType(companyRechargesBean);
    }

    /**
     * 根据ID更新企业订单
     *
     * @param companyRechargesBean
     * @return
     */
    public int updateByPrimaryKeySelective(CompanyRechargesBean companyRechargesBean) {
        return companyRechargesWriterMapper.updateByPrimaryKeySelective(companyRechargesBean);
    }

    /**
     * 根据订薪周期查询是否存在某个状态的订单
     *
     * @param companyRechargesBean
     * @return
     */
    public int selectCountByCondition(CompanyRechargesBean companyRechargesBean) {
        return companyRechargesReaderMapper.selectCountByCondition(companyRechargesBean);
    }

    /**
     * 更新垫付与实际支付额度
     *
     * @param record
     * @return
     */
    public int updateScaleAndRealAmount(CompanyRechargesBean record) {
        return companyRechargesWriterMapper.updateScaleAndRealAmount(record);
    }

    /**
     * 根据订薪周期查询是否存在某个状态的订单信息
     *
     * @param companyRechargesBean
     * @return
     */
    public List<CompanyRechargesBean> selectListByCondition(CompanyRechargesBean companyRechargesBean) {
        return companyRechargesReaderMapper.selectListByCondition(companyRechargesBean);
    }

    /**
     * 企业发工资生成订单,并返回订单本身
     *
     * @param companyId      公司Id
     * @param rsourceId      来源Id-工资单主键
     * @param totalWages     金额
     * @param rechargeType   类型 CompanyRechargeConstant
     * @param zeroBigDeciaml 红包金额
     * @param rechargeName   订单名称
     * @return
     */
    public CompanyRechargesBean generateOrder(Long companyId, Long rsourceId, int rechargeType, BigDecimal totalWages,
                                              BigDecimal zeroBigDeciaml, String rechargeName) {

        //检查订单是否已生成
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        //类型
        companyRechargesBean.setRechargeType(rechargeType);
        //周期ID
        companyRechargesBean.setExcelId(rsourceId);
        List<CompanyRechargesBean> list = companyRechargesService.selectByExcelIdAndType(companyRechargesBean);
        if (list != null && list.size() > 0) {
            throw new BusinessException("该工资单已经生成订单");
        }
        //充值金额
        companyRechargesBean.setRechargeMoney(totalWages);
        //企业ID
        companyRechargesBean.setRechargeCompanyId(companyId);
        //创建时间
        companyRechargesBean.setRechargeAddtime(new Date());
        //订单号
        String code = idGeneratorService.getOrderId(BusinessEnum.SEND_SALARY);
        companyRechargesBean.setRechargeNumber(code);
        //状态
        companyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
        companyRechargesBean.setRechargeName(rechargeName);
//

        //TODO 到时候记得添加红包
//        RedEnvelopeBean redEnvelopeBean=redEnvelopeService.getByCompanyId(companyId,CompanyRechargeConstant.SEND_SALARY_TYPE);
//        if(redEnvelopeBean!=null && redEnvelopeBean.getRedMoney()!=null){
//            companyRechargesBean.setRedWalletAmount(redEnvelopeBean.getRedMoney());
//        }else{
        companyRechargesBean.setRedWalletAmount(zeroBigDeciaml);
//        }
        //垫付金额,垫付比例
        CompanyRechargesBean paymentBean = queryPaymentInfo(companyId, totalWages);
        companyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
        companyRechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
        //实际支付额度
        companyRechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
        int result = companyRechargesWriterMapper.insertSelective(companyRechargesBean);
        if (result <= 0) {
            throw new BusinessException("新增发工资订单失败");
        }
        return companyRechargesBean;
    }

    /**
     * 获取垫付金额及垫付比例
     *
     * @param companyId
     * @param totalWages
     * @return
     */
    public CompanyRechargesBean queryPaymentInfo(Long companyId, BigDecimal totalWages) {
        BigDecimal zeroBigDeciaml = new BigDecimal("0");
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
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
                    BigDecimal rechargePaymentAmount = paymentRate.multiply(totalWages).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
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
        LOGGER.info("新建发工资订单,获取垫付比例:" + companyRechargesBean.getRechargePaymentRate() + ",获取垫付金额:" + companyRechargesBean.getRechargePaymentAmount());
        //实际支付额度
        companyRechargesBean.setRechargeRealAmount(totalWages.subtract(companyRechargesBean.getRechargePaymentAmount()));
        return companyRechargesBean;
    }
}
