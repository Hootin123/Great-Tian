package com.xtr.core.task;

import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.GrantStateConstant;
import com.xtr.comm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
 * <p>公司发工资扣款</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/11 16:57
 */
@Service("payrollDebitTask")
public class PayrollDebitTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollDebitTask.class);

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CustomerSalarysService customerSalarysService;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private SubAccountService subAccountService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    /**
     * 扣款
     *
     * @throws Exception
     */
    @Override
    public void run() throws Exception {
        LOGGER.info("开始执行公司发工资扣款任务···");
        String sameDay = DateUtil.formatCurrentDate();
        //获取获取所有需要扣款的工资单
        List<CompanySalaryExcelBean> list = companySalaryExcelService.getPendingPayroll(sameDay, GrantStateConstant.GRANT_STATE_4);
        if (!list.isEmpty()) {
            LOGGER.info("【" + sameDay + "】需要发工资扣款的数量【" + list.size() + "】");
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
            for (final CompanySalaryExcelBean companySalaryExcelBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //发工资扣款
                            debit(companySalaryExcelBean);
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
        LOGGER.info("结束执行公司发工资扣款任务···");
    }

    /**
     * 发工资扣款
     *
     * @param companySalaryExcelBean
     */
    @Transactional
    private void debit(CompanySalaryExcelBean companySalaryExcelBean) {
        //根据excelId获取工资单
        List<CustomerSalarysBean> list = customerSalarysService.selectByExcelId(companySalaryExcelBean.getExcelId());
        if (!list.isEmpty()) {
            LOGGER.error("ExcelId【" + companySalaryExcelBean.getExcelId() + "】开始进行扣款···");
            //检查企业代发协议是否正常
            checkCompany(companySalaryExcelBean);
            //扣款总金额
            BigDecimal totleCost = getTotleCost(list);
            //检查企业账户余额是否足够,如果足够，直接扣款  记录产生来源 1充值+ 2提现- 3公司发工资',
            subAccountService.deduct(companySalaryExcelBean.getExcelCompanyId(), totleCost, 3, companySalaryExcelBean.getExcelId(), AccountType.COMPANY);
            //更新工资单发放状态 --已发放工资单 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
            companySalaryExcelService.updateGrantState(companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_1, GrantStateConstant.GRANT_STATE_4);
            //更新工资单实发总额
            companySalaryExcelService.updateSalaryTotal(companySalaryExcelBean.getExcelId(), totleCost);
            //更新收支记录状态
            companyRechargesService.updateRechargeState(companySalaryExcelBean.getExcelCompanyId(), companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_2);
            LOGGER.error("ExcelId【" + companySalaryExcelBean.getExcelId() + "】扣款成功···");
        } else {
            LOGGER.error("ExcelId【" + companySalaryExcelBean.getExcelId() + "】没有生成对应的工资单");
        }
    }

    /**
     * 检查企业账户协议状态
     *
     * @param companySalaryExcelBean
     */
    private void checkCompany(CompanySalaryExcelBean companySalaryExcelBean) {
        ////签约类型：1代发协议 2垫发协议',
        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectIsUserFulByTypeAndTime(companySalaryExcelBean.getExcelCompanyId(), 1, DateUtil.getCurrDateOfDate(DateUtil.dateString));
        if (companyProtocolsBean != null) {
            //协议状态:1待审批 2签约 3即将到期 4合约到期 5冻结
            if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 1) {
                throw new BusinessException("CompanyId【" + companySalaryExcelBean.getExcelCompanyId() + "】代发协议还未审批，不能进行工资代发业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 4) {
                throw new BusinessException("CompanyId【" + companySalaryExcelBean.getExcelCompanyId() + "】代发协议合约已到期，不能进行工资代发业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 5) {
                throw new BusinessException("CompanyId【" + companySalaryExcelBean.getExcelCompanyId() + "】代发协议合约已被冻结，不能进行工资代发业务");
            }
        } else {
            throw new BusinessException("CompanyId【" + companySalaryExcelBean.getExcelCompanyId() + "】未签署代发协议，不能进行工资代发业务");
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
