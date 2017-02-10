package com.xtr.task.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.GrantStateConstant;
import com.xtr.comm.constant.RecordSourceConstant;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>发工资Job</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 16:27
 */
public class PayDayTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayDayTask.class);

    private CompanySalaryExcelService companySalaryExcelService;

    private CustomerSalarysService customerSalarysService;

    private SubAccountService subAccountService;

    private CustomerRechargesService customerRechargesService;

    private CompanyRechargesService companyRechargesService;

    /**
     * 创建线程池
     */
    //    ExecutorService pool = Executors.newCachedThreadPool();
    ExecutorService pool = null;


    public PayDayTask() {
        super();
        companySalaryExcelService = (CompanySalaryExcelService) SpringUtils.getBean("companySalaryExcelService");
        customerSalarysService = (CustomerSalarysService) SpringUtils.getBean("customerSalarysService");
        subAccountService = (SubAccountService) SpringUtils.getBean("subAccountService");
        customerRechargesService = (CustomerRechargesService) SpringUtils.getBean("customerRechargesService");
        companyRechargesService = (CompanyRechargesService) SpringUtils.getBean("companyRechargesService");
    }

    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        String sameDay = DateUtil.formatCurrentDate();
        //获取状态为仅发放工资单的数据
        LOGGER.info("开始执行【" + sameDay + "】发工资任务");
        List<CompanySalaryExcelBean> list = companySalaryExcelService.getPendingPayroll(sameDay, GrantStateConstant.GRANT_STATE_4);
        if (!list.isEmpty()) {
            LOGGER.info("【" + sameDay + "】需要发工资的数量【" + list.size() + "】");
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            for (CompanySalaryExcelBean companySalaryExcelBean : list) {
                //工资发放
                payOff(companySalaryExcelBean);
            }
        }
        LOGGER.info("结束执行【" + sameDay + "】发工资任务");
    }

    /**
     * 工资发放
     *
     * @param companySalaryExcelBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void payOff(final CompanySalaryExcelBean companySalaryExcelBean) throws Exception {
        //根据excelId获取工资单
        List<CustomerSalarysBean> list = customerSalarysService.selectByExcelId(companySalaryExcelBean.getExcelId());
        if (list != null) {
            try {
                BigDecimal totleCost = getTotleCost(list);
                //检查企业账户余额是否足够,如果足够，直接扣款  记录产生来源 1充值+ 2提现- 3发工资 4公司发工资+ 5预支工资还款- 6购买理财- 7理财还款+ 8得到返现',
                subAccountService.deduct(companySalaryExcelBean.getExcelCompanyId(), totleCost, CompanyRechargeConstant.SEND_SALARY_TYPE, companySalaryExcelBean.getExcelId(), AccountType.COMPANY);

                final CustomerRechargesBean customerRechargesBean = new CustomerRechargesBean();
                final String batchNo = UUID.randomUUID().toString();
                final List<Exception> errorList = new ArrayList();
//            int waitTime = 500;
                List<Future> rowResult = new CopyOnWriteArrayList<Future>();
                for (final CustomerSalarysBean customerSalarysBean : list) {
                    rowResult.add(pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //发工资
                                payday(customerSalarysBean, customerRechargesBean, batchNo);
                            } catch (Exception e) {
                                //添加一次信息
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
                //出现异常
                if (!errorList.isEmpty()) {
                    //数据回滚处理

                    //抛出异常信息
                    throw new BusinessException(errorList.get(0).getMessage());
                } else {
                    //更新工资单发放状态 --已发放工资单 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
                    companySalaryExcelService.updateGrantState(companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_2, GrantStateConstant.GRANT_STATE_4);
                    //更新工资单实发总额
                    companySalaryExcelService.updateSalaryTotal(companySalaryExcelBean.getExcelId(), totleCost);
                    //更新收支记录状态
                    companyRechargesService.updateRechargeState(companySalaryExcelBean.getExcelCompanyId(), companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_2);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new BusinessException(e.getMessage());
            }

        } else {
            throw new BusinessException("ExcelId【" + companySalaryExcelBean.getExcelId() + "】没有生成对应的工资单");
        }
    }

    /**
     * 发工资
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void payday(CustomerSalarysBean customerSalarysBean, CustomerRechargesBean customerRechargesBean, String batchNo) {
        //获取员工信息  账户性质 1-个人 2-企业
        SubAccountBean subAccountBean = subAccountService.selectByCustId(customerSalarysBean.getSalaryCustomerId(), 1);
        if (subAccountBean != null) {
            //给企业员工发工资
            LOGGER.info("用户充值ID：" + customerSalarysBean.getSalaryCustomerId() + "   " + JSON.toJSONString(customerSalarysBean));
            subAccountService.rechargeRecords(customerSalarysBean.getSalaryCustomerId(), customerSalarysBean.getSalaryActual(), RecordSourceConstant.RECORD_SOURCE_4, customerSalarysBean.getSalaryId(), AccountType.PEOPLE);
            //判断员工是否设置自动提现  是否自动转入 0:非自动 1;自动
            if (subAccountBean.getIsAutoTransfer().intValue() == 1) {
                //用户id
                customerRechargesBean.setRechargeCustomerId(customerSalarysBean.getSalaryCustomerId());
                //批次号
                customerRechargesBean.setBatchNumber(batchNo);
                //操作类型 类型 1充值 2提现
                customerRechargesBean.setRechargeType(CompanyRechargeConstant.WITHDRAW_TYPE);
                //注备
                customerRechargesBean.setRechargeBak("自动提现");
                //提现金额
                customerRechargesBean.setRechargeMoney(customerSalarysBean.getSalaryActual());
                LOGGER.info("提现申请:" + JSON.toJSONString(customerRechargesBean));
                //发起提现申请
                customerRechargesService.customerWithdrawals(customerRechargesBean);
            }
        } else {
            LOGGER.info("用户ID：" + customerSalarysBean.getSalaryCustomerId() + "账户不存在，无法进行工资发放");
        }
    }

    /**
     * 获取总工资
     *
     * @param list
     */
    private BigDecimal getTotleCost(List<CustomerSalarysBean> list) {
        if (!list.isEmpty()) {
            BigDecimal totleCost = new BigDecimal(0);
            for (CustomerSalarysBean customerSalarysBean : list) {
                //实发工资
                totleCost = totleCost.add(customerSalarysBean.getSalaryActual());
            }
            return totleCost;
        }
        return new BigDecimal(0);
    }

}
