package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.dto.gateway.response.DefrayPayResponse;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersSupplementService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.core.persistence.writer.customer.CustomerRechargesWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by allycw3 on 2016/10/9.
 */
@Service("jdWithdrawalsBackTask")
public class JdWithdrawalsBackTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdWithdrawalsBackTask.class);

    @Resource
    private CustomerRechargesService customerRechargesService;

    @Resource
    private CustomerRechargesWriterMapper customerRechargesWriterMapper;

    @Resource
    private CustomersService customersService;

    @Resource
    private BankCodeService bankCodeService;

    @Resource
    private JdPayService jdPayService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private CustomersSupplementService customersSupplementService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    /**
     * 提现
     *
     * @throws Exception
     */
    public void run() throws Exception {
        LOGGER.info("开始执行京东社保公积金补退提现任务···");
        //获取所有的提现申请
        List<CustomerRechargesBean> list = customerRechargesService.getBackWithdrawals(Integer.valueOf(2));
        if (!list.isEmpty()) {
            int size = list.size();
            LOGGER.info("社保公积金补退提现申请数量【" + size + "】");
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
            for (final CustomerRechargesBean customerRechargesBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //京东提现
                            withdrawals(customerRechargesBean);
                        } catch (Exception e) {
                            //添加异常信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }));
            }

            //等待处理结果
            for (Future f : rowResult) {
                f.get();
            }
            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
            pool.shutdown();

            if (!errorList.isEmpty()) {
                //抛出异常信息
                throw new BusinessException(errorList.get(0).getMessage());
            }
        }
        LOGGER.info("京东社保公积金补退提现任务执行结束···");
    }

    /**
     * 京东提现
     *
     * @param customerRechargesBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void withdrawals(CustomerRechargesBean customerRechargesBean) {
        DefrayPayRequest dr = new DefrayPayRequest(BusinessType.CUSTOMER_WITHDRAW);
        //根据Id获取员工姓名
        CustomersBean customersBean = customersService.selectNameById(customerRechargesBean.getRechargeCustomerId());
        //根据银行名称获取银行编码
        BankCodeBean bankCodeBean = bankCodeService.selectByBankName(customerRechargesBean.getRechargeBank());
        if (bankCodeBean != null)
            //收款银行编码
            dr.setPayeeBankCode(bankCodeBean.getBankCode());
        //收款帐户类型  对私户=P；对公户=C
        dr.setPayeeAccountType("P");
        //收款帐户号
        dr.setPayeeAccountNo(customerRechargesBean.getRechargeBanknumber());
        //收款帐户名称
        if (customersBean != null)
            dr.setPayeeAccountName(customersBean.getCustomerTurename());
        //订单ID 唯一
        dr.setOutTradeNo(customerRechargesBean.getRechargeNumber());
        //订单交易金额  单位：分，大于0
        dr.setTradeAmount(customerRechargesBean.getRechargeMoney().multiply(new BigDecimal(100)).longValue() + "");
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
            customerRechargesBean.setRechargeState(Integer.valueOf(2));
            //回调结果 0待回调 1成功 2....各种错误定义
            customerRechargesBean.setRechargeRecallResult(Integer.valueOf(0));
        } else {
            //失败 判断是否客户原因
            if (CodeConst.isCustomerReasons(defrayPayResponse.getResponseCode())) {
                //客户原因提现失败，下次将不再发起提现  状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 5付款失败账户信息错误
                customerRechargesBean.setRechargeState(Integer.valueOf(1));
                //初审备注
                customerRechargesBean.setRechargeAuditFirstRemark(defrayPayResponse.getResponseMessage());
            } else {
                //重置订单号
                customerRechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS));
            }
            //更新失败信息
            CustomersSupplementBean customersSupplementBean=new CustomersSupplementBean();
            customersSupplementBean.setSupplementId(customerRechargesBean.getResourceId());
            customersSupplementBean.setSupplementFailMsg(defrayPayResponse.getResponseMessage());
            customersSupplementBean.setSupplementPayStatus(1);
            customersSupplementService.updateByPrimaryKeySelective(customersSupplementBean);

//            CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();
//            companyShebaoOrderBean.setId(customerRechargesBean.getResourceId());
//            companyShebaoOrderBean.setShebaoCustomerPayStatus(1);
//            companyShebaoOrderBean.setShebaoFailMsg(defrayPayResponse.getResponseMessage());
//            companyShebaoService.updateByPrimaryKeySelective(companyShebaoOrderBean);
    }
        customerRechargesWriterMapper.updateByPrimaryKeySelective(customerRechargesBean);

    }
}
