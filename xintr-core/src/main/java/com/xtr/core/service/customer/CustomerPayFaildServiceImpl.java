package com.xtr.core.service.customer;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.customer.CustomerUnPayOrderDto;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.dto.gateway.response.DefrayPayResponse;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.customer.CustomerPayFaildService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersSupplementService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.core.persistence.reader.customer.CustomerRechargesReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.reader.salary.RapidlyPayrollReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerRechargesWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollExcelWriterMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/** 发工资失败补发
 * @Author Xuewu
 * @Date 2016/8/30.
 */
@Service("customerPayFaildService")
public class CustomerPayFaildServiceImpl implements CustomerPayFaildService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerPayFaildServiceImpl.class);

    @Resource
    private CustomerPayrollReaderMapper customerPayrollReaderMapper;

    @Resource
    private CustomerRechargesReaderMapper customerRechargesReaderMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CustomerRechargesWriterMapper customerRechargesWriterMapper;

    @Resource
    private CustomersService customersService;

    @Resource
    private BankCodeService bankCodeService;

    @Resource
    private JdPayService jdPayService;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private RapidlyPayrollWriterMapper rapidlyPayrollWriterMapper;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private RapidlyPayrollReaderMapper rapidlyPayrollReaderMapper;

    @Resource
    private CustomersSupplementService customersSupplementService;

    @Override
    public ResultResponse selectPayFailedOrder(CustomerUnPayOrderDto dto) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(dto.getPageIndex(), dto.getPageSize());
        PageList<Map<String, Object>> pages = customerPayrollReaderMapper.selectPayFailedOrderList(dto, pageBounds);
        resultResponse.setSuccess(true);
        resultResponse.setData(pages);
        resultResponse.setPaginator(pages.getPaginator());
        return resultResponse;
    }

    @Override
    @Transactional
    public boolean sysMakeUp(Long rechargeId, SysUserBean loginUserObj) throws BusinessException {
        CustomerRechargesBean rechargesBean = customerRechargesReaderMapper.selectByPrimaryKey(rechargeId);
        if(rechargesBean == null)
            throw new BusinessException("订单信息不存在");
        if(rechargesBean.getRechargeType() != 2 || null == rechargesBean.getResourceId() || 1 != rechargesBean.getRechargeState())
            throw new BusinessException("订单状态错误");

        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(rechargesBean.getRechargeCustomerId());
        //更新为用户最新工资卡信息
        rechargesBean.setRechargeBanknumber(customersBean.getCustomerBanknumber());
        rechargesBean.setRechargeBank(customersBean.getCustomerBank());
        rechargesBean.setRechargeBankarea(customersBean.getCustomerBankarea());

        //更新状态信息
        rechargesBean.setRechargeRecallResult(0);
        rechargesBean.setRechargeState(2);
        rechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS));
        customerRechargesWriterMapper.updateByPrimaryKey(rechargesBean);

        //操作人
        CustomerPayrollBean customerPayrollBean = new CustomerPayrollBean();
        customerPayrollBean.setId(rechargesBean.getResourceId());
        customerPayrollBean.setFinanceUser(loginUserObj.getUserName());

        DefrayPayRequest dr = new DefrayPayRequest(BusinessType.CUSTOMER_WITHDRAW);
        //根据银行名称获取银行编码
        BankCodeBean bankCodeBean = bankCodeService.selectByBankName(rechargesBean.getRechargeBank());
        if (bankCodeBean != null)
            //收款银行编码
            dr.setPayeeBankCode(bankCodeBean.getBankCode());
        //收款帐户类型  对私户=P；对公户=C
        dr.setPayeeAccountType("P");
        //收款帐户号
        dr.setPayeeAccountNo(rechargesBean.getRechargeBanknumber());
        //收款帐户名称
        if (customersBean != null)
            dr.setPayeeAccountName(customersBean.getCustomerTurename());
        //订单ID 唯一
        dr.setOutTradeNo(rechargesBean.getRechargeNumber());
        //订单交易金额  单位：分，大于0
        dr.setTradeAmount(rechargesBean.getRechargeMoney().multiply(new BigDecimal(100)).setScale(0).toString());
        //订单摘要  商品描述，订单标题，关键描述信息
        dr.setTradeSubject("发工资自动提现");
        //收款卡种  借记卡=DE；信用卡=CR
        dr.setPayeeCardType("DE");

        //提现
//        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);
        LOGGER.debug("京东提现参数：" + JSON.toJSONString(dr));
        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);
        //交易状态
//        public static final String TRADE_FINI="FINI"; //交易成功      成功
//        public static final String TRADE_CLOS="CLOS"; //交易关闭      失败
//        public static final String TRADE_WPAR="WPAR"; //等待支付结果   等待
//        public static final String TRADE_BUID="BUID"; //交易建立       等待
//        public static final String TRADE_ACSU="ACSU"; //已受理         等待
        if (!StringUtils.equals(defrayPayResponse.getTradeStatus(), CodeConst.TRADE_CLOS)) {
            //成功或等待  将提现状态更改为初审通过  状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 5付款失败账户信息错误
            rechargesBean.setRechargeState(2);
            //回调结果 0待回调 1成功 2....各种错误定义
            rechargesBean.setRechargeRecallResult(0);
        } else {
            //失败 判断是否客户原因
            if (CodeConst.isCustomerReasons(defrayPayResponse.getResponseCode())) {
                //客户原因提现失败，下次将不再发起提现  状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 5付款失败账户信息错误
                rechargesBean.setRechargeState(1);
                //初审备注
                rechargesBean.setRechargeAuditFirstRemark(defrayPayResponse.getResponseMessage());
            }else{
                rechargesBean.setRechargeState(0);
                rechargesBean.setRechargeAuditFirstRemark(defrayPayResponse.getResponseMessage());
                rechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS));
            }
            customerPayrollBean.setFailMsg(defrayPayResponse.getResponseMessage());
            customerPayrollBean.setPayStatus(1);
        }
        //更新提现申请
        customerRechargesWriterMapper.updateByPrimaryKeySelective(rechargesBean);

        customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);

        return true;
    }

    @Override
    public boolean handMakeUp(long rechargeId, String remark, SysUserBean user) throws BusinessException {
        CustomerRechargesBean rechargesBean = customerRechargesReaderMapper.selectByPrimaryKey(rechargeId);
        if(rechargesBean == null)
            throw new BusinessException("订单信息不存在");
        if(rechargesBean.getRechargeType() != 2 || null == rechargesBean.getResourceId() || 1 != rechargesBean.getRechargeState())
            throw new BusinessException("订单状态错误");
        if(StringUtils.isBlank(remark))
            throw new BusinessException("请输入备注信息");

        //操作人
        CustomerPayrollBean customerPayrollBean = new CustomerPayrollBean();
        customerPayrollBean.setId(rechargesBean.getResourceId());
        customerPayrollBean.setFinanceUser(user.getUserName());
        customerPayrollBean.setRemark(remark);

        //更新状态信息
        rechargesBean.setRechargeRecallResult(1);
        rechargesBean.setRechargeState(2);
        customerRechargesWriterMapper.updateByPrimaryKeySelective(rechargesBean);

        customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);

        return true;
    }

    /**
     * 自动处理社保公积金个人补退
     * @param rechargeId
     * @param loginUserObj
     * @return
     * @throws BusinessException
     */
    @Transactional
    public boolean sysMakeUpShebao(Long rechargeId, SysUserBean loginUserObj) throws BusinessException {
        CustomerRechargesBean rechargesBean = customerRechargesReaderMapper.selectByPrimaryKey(rechargeId);
        if(rechargesBean == null)
            throw new BusinessException("订单信息不存在");
        if(rechargesBean.getRechargeType() != 8 || null == rechargesBean.getResourceId() || 1 != rechargesBean.getRechargeState())
            throw new BusinessException("订单状态错误");

        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(rechargesBean.getRechargeCustomerId());
        //更新为用户最新工资卡信息
        rechargesBean.setRechargeBanknumber(customersBean.getCustomerBanknumber());
        rechargesBean.setRechargeBank(customersBean.getCustomerBank());
        rechargesBean.setRechargeBankarea(customersBean.getCustomerBankarea());

        //更新状态信息
        rechargesBean.setRechargeRecallResult(0);
        rechargesBean.setRechargeState(2);
        rechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS));
        //更新提现申请
        customerRechargesWriterMapper.updateByPrimaryKeySelective(rechargesBean);

        //操作人
        CustomersSupplementBean customersSupplementBean=new CustomersSupplementBean();
        customersSupplementBean.setSupplementId(rechargesBean.getResourceId());
        customersSupplementBean.setSupplementFinanceUser(loginUserObj.getUserName());
//        CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();
//        companyShebaoOrderBean.setId(rechargesBean.getResourceId());
//        companyShebaoOrderBean.setShebaoFinanceUser(loginUserObj.getUserName());

        DefrayPayRequest dr = new DefrayPayRequest(BusinessType.CUSTOMER_WITHDRAW);
        //根据Id获取员工姓名
//        CustomersBean customersBean = customersService.selectNameById(rechargesBean.getRechargeCustomerId());
        //根据银行名称获取银行编码
        BankCodeBean bankCodeBean = bankCodeService.selectByBankName(rechargesBean.getRechargeBank());
        if (bankCodeBean != null)
            //收款银行编码
            dr.setPayeeBankCode(bankCodeBean.getBankCode());
        //收款帐户类型  对私户=P；对公户=C
        dr.setPayeeAccountType("P");
        //收款帐户号
        dr.setPayeeAccountNo(rechargesBean.getRechargeBanknumber());
        //收款帐户名称
        if (customersBean != null)
            dr.setPayeeAccountName(customersBean.getCustomerTurename());
        //订单ID 唯一
        dr.setOutTradeNo(rechargesBean.getRechargeNumber());
        //订单交易金额  单位：分，大于0
        dr.setTradeAmount(rechargesBean.getRechargeMoney().multiply(new BigDecimal(100)).longValue() + "");
        //订单摘要  商品描述，订单标题，关键描述信息
        dr.setTradeSubject("社保公积金补退自动提现");
        //收款卡种  借记卡=DE；信用卡=CR
        dr.setPayeeCardType("DE");

        //提现
//        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);
        LOGGER.debug("京东社保公积金补退提现参数：" + JSON.toJSONString(dr));
        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);

        //交易状态
        if (!StringUtils.equals(defrayPayResponse.getTradeStatus(), CodeConst.TRADE_CLOS)) {
            //成功或等待  将提现状态更改为初审通过  状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 5付款失败账户信息错误
            rechargesBean.setRechargeState(Integer.valueOf(2));
            //回调结果 0待回调 1成功 2....各种错误定义
            rechargesBean.setRechargeRecallResult(Integer.valueOf(0));
        } else {
            //失败 判断是否客户原因
            if (CodeConst.isCustomerReasons(defrayPayResponse.getResponseCode())) {
                //客户原因提现失败，下次将不再发起提现  状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 5付款失败账户信息错误
                rechargesBean.setRechargeState(Integer.valueOf(1));
                //初审备注
                rechargesBean.setRechargeAuditFirstRemark(defrayPayResponse.getResponseMessage());
            }else{
                rechargesBean.setRechargeState(0);
                rechargesBean.setRechargeAuditFirstRemark(defrayPayResponse.getResponseMessage());
                rechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS));
            }
//            companyShebaoOrderBean.setShebaoCustomerPayStatus(1);
//            companyShebaoOrderBean.setShebaoFailMsg(defrayPayResponse.getResponseMessage());
            customersSupplementBean.setSupplementFailMsg(defrayPayResponse.getResponseMessage());
            customersSupplementBean.setSupplementPayStatus(1);


        }
        //更新提现申请
        customerRechargesWriterMapper.updateByPrimaryKeySelective(rechargesBean);
        //更新企业社保公积金订单信息
//        companyShebaoService.updateByPrimaryKeySelective(companyShebaoOrderBean);
        customersSupplementService.updateByPrimaryKeySelective(customersSupplementBean);
        return true;
    }

    /**
     * 手动处理社保公积金个人补退
     * @param rechargeId
     * @param remark
     * @param user
     * @return
     * @throws BusinessException
     */
    public boolean handMakeUpShebao(long rechargeId, String remark, SysUserBean user) throws BusinessException {
        CustomerRechargesBean rechargesBean = customerRechargesReaderMapper.selectByPrimaryKey(rechargeId);
        if(rechargesBean == null)
            throw new BusinessException("订单信息不存在");
        if(rechargesBean.getRechargeType() != 8 || null == rechargesBean.getResourceId() || 1 != rechargesBean.getRechargeState())
            throw new BusinessException("订单状态错误");
        if(StringUtils.isBlank(remark))
            throw new BusinessException("请输入备注信息");

        //操作人
//        CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();
//        companyShebaoOrderBean.setId(rechargesBean.getResourceId());
//        companyShebaoOrderBean.setShebaoFinanceUser(user.getUserName());
//        companyShebaoOrderBean.setShebaoRemark(remark);
        CustomersSupplementBean customersSupplementBean=new CustomersSupplementBean();
        customersSupplementBean.setSupplementId(rechargesBean.getResourceId());
        customersSupplementBean.setSupplementFinanceUser(user.getUserName());
        customersSupplementBean.setSupplementRemark(remark);

        //更新状态信息
        rechargesBean.setRechargeRecallResult(1);
        rechargesBean.setRechargeState(2);
        customerRechargesWriterMapper.updateByPrimaryKeySelective(rechargesBean);

        //更新企业社保公积金订单信息
        customersSupplementService.updateByPrimaryKeySelective(customersSupplementBean);

        return true;
    }

    /**
     * 手动处理急速发工资
     * @param rechargeId
     * @param remark
     * @param user
     * @return
     * @throws BusinessException
     */
    public boolean handMakeUpRapidly(long rechargeId, String remark, SysUserBean user) throws BusinessException {
        RapidlyPayrollBean rapidlyPayrollBean=new RapidlyPayrollBean();
        rapidlyPayrollBean.setId(rechargeId);
        rapidlyPayrollBean.setFinanceUser(user.getId());
        rapidlyPayrollBean.setRemark(remark);
        rapidlyPayrollBean.setIsPayOff(1);//已发
        rapidlyPayrollBean.setCallbackState(1);//成功
        rapidlyPayrollWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollBean);
        return true;
    }

    /**
     * 自动处理急速发工资
     * @param rechargeId
     * @param loginUserObj
     * @return
     * @throws BusinessException
     */
    public boolean sysMakeUpRapidly(Long rechargeId, SysUserBean loginUserObj) throws BusinessException {
//        RapidlyPayrollBean rapidlyPayrollBean=new RapidlyPayrollBean();
//        rapidlyPayrollBean.setId(rechargeId);
//        rapidlyPayrollBean.setFinanceUser(loginUserObj.getId());
//        rapidlyPayrollBean.setIsPayOff(0);//未发
//        rapidlyPayrollWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollBean);
//        return true;
        RapidlyPayrollBean rapidlyPayrollBean=rapidlyPayrollReaderMapper.selectByPrimaryKeyId(rechargeId);
        if(rapidlyPayrollBean == null)
            throw new BusinessException("急速工资信息不存在");
        if( null == rapidlyPayrollBean.getIsPayOff() || 1 != rapidlyPayrollBean.getIsPayOff().intValue() || rapidlyPayrollBean.getCallbackState()==null || rapidlyPayrollBean.getCallbackState().intValue()!=2)
            throw new BusinessException("状态错误");

//        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(rechargesBean.getRechargeCustomerId());
        //更新为用户最新工资卡信息
//        rapidlyPayrollBean.setBankNumber(customersBean.getCustomerBanknumber());
//        rapidlyPayrollBean.setBankAccount(customersBean.getCustomerBank());

        //更新状态信息
//        rapidlyPayrollBean.setIsPayOff(1);
//        rapidlyPayrollBean.setCallbackState(0);
        rapidlyPayrollBean.setOrderNo(UUID.randomUUID().toString());
        //更新提现申请
//        customerRechargesWriterMapper.updateByPrimaryKeySelective(rechargesBean);

        //操作人
//        CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();
//        companyShebaoOrderBean.setId(rechargesBean.getResourceId());
//        companyShebaoOrderBean.setShebaoFinanceUser(loginUserObj.getUserName());

        rapidlyPayrollBean.setFinanceUser(loginUserObj.getId());



        DefrayPayRequest dr = new DefrayPayRequest(BusinessType.RAPIDLY_WITHDRAW);
        //根据Id获取员工姓名
//        CustomersBean customersBean = rapidlyPayrollBean.getCustomerName();
        //根据银行名称获取银行编码
        BankCodeBean bankCodeBean = bankCodeService.selectByBankName(rapidlyPayrollBean.getBankAccount());
        if (bankCodeBean != null)
            //收款银行编码
            dr.setPayeeBankCode(bankCodeBean.getBankCode());
        //收款帐户类型  对私户=P；对公户=C
        dr.setPayeeAccountType("P");
        //收款帐户号
        dr.setPayeeAccountNo(rapidlyPayrollBean.getBankNumber());
        //收款帐户名称
        dr.setPayeeAccountName(rapidlyPayrollBean.getCustomerName());
        //订单ID 唯一
        dr.setOutTradeNo(rapidlyPayrollBean.getOrderNo());
        //订单交易金额  单位：分，大于0
        dr.setTradeAmount(rapidlyPayrollBean.getRealWage().multiply(new BigDecimal(100)).longValue() + "");
        //订单摘要  商品描述，订单标题，关键描述信息
        dr.setTradeSubject("发工资自动提现");
        //收款卡种  借记卡=DE；信用卡=CR
        dr.setPayeeCardType("DE");

        //提现
//        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);
        LOGGER.debug("京东提现参数：" + JSON.toJSONString(dr));
        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);

        if (!StringUtils.equals(defrayPayResponse.getTradeStatus(), CodeConst.TRADE_CLOS)) {
            //工资单是否已发 0:未发 1:已发
            rapidlyPayrollBean.setIsPayOff(1);
            //回调结果 0待回调 1成功 2....各种错误定义
            rapidlyPayrollBean.setCallbackState(Integer.valueOf(0));
        } else {
            //失败 判断是否客户原因
            if (CodeConst.isCustomerReasons(defrayPayResponse.getResponseCode())) {
                //工资单是否已发 0:未发 1:已发
                rapidlyPayrollBean.setIsPayOff(1);
                //回调结果 0待回调 1成功 2....各种错误定义
                rapidlyPayrollBean.setCallbackState(Integer.valueOf(2));
            }else {
                //重置订单号
                rapidlyPayrollBean.setOrderNo(UUID.randomUUID().toString());
                rapidlyPayrollBean.setCallbackState(Integer.valueOf(-1));
                rapidlyPayrollBean.setIsPayOff(0);
            }
            rapidlyPayrollBean.setPayStatus(1);
            rapidlyPayrollBean.setFailMsg(defrayPayResponse.getResponseMessage());
        }
        //更新提现申请
        rapidlyPayrollWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollBean);
        return true;
    }
}
