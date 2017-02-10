package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.GrantStateConstant;
import com.xtr.comm.constant.RecordSourceConstant;
import com.xtr.comm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
@Service("payDayTask")
public class PayDayTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayDayTask.class);

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;
    @Resource
    private CustomerSalarysService customerSalarysService;
    @Resource
    private SubAccountService subAccountService;
    @Resource
    private CustomerRechargesService customerRechargesService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;


    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        String sameDay = DateUtil.formatCurrentDate();
        //获取工资单状态为发放中的数据
        LOGGER.info("开始执行【" + sameDay + "】发工资任务");
        //工资单发放状态 --已发放工资单 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
        List<CompanySalaryExcelBean> list = companySalaryExcelService.getPendingPayroll(sameDay, GrantStateConstant.GRANT_STATE_1);
        if (!list.isEmpty()) {
            LOGGER.info("【" + sameDay + "】需要发工资的数量【" + list.size() + "】");
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);

            for (final CompanySalaryExcelBean companySalaryExcelBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //工资发放
                            payOff(companySalaryExcelBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("ExcelId【" + companySalaryExcelBean.getExcelId() + "】出现异常···" + e.getMessage(), e);
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
                //抛出异常信息
                throw new BusinessException(errorList.get(0).getMessage());
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
        if (!list.isEmpty()) {
            LOGGER.info("ExcelId【" + companySalaryExcelBean.getExcelId() + "】开始发工资···");
            String batchNo = UUID.randomUUID().toString();
            for (CustomerSalarysBean customerSalarysBean : list) {
                //工资是否已发放  0:未发放  1:已发放
                if (customerSalarysBean.getIssue().intValue() == 0) {
                    //发工资
                    payday(customerSalarysBean, batchNo);
                }
            }
            LOGGER.info("将ExcelId【" + companySalaryExcelBean.getExcelId() + "】状态改为已发放···");
            //更新工资单发放状态 --已发放工资单 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
            companySalaryExcelService.updateGrantState(companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_2, GrantStateConstant.GRANT_STATE_1);
        } else {
            LOGGER.error("ExcelId【" + companySalaryExcelBean.getExcelId() + "】没有生成对应的工资单");
        }
    }

    /**
     * 发工资
     */
    public void payday(CustomerSalarysBean customerSalarysBean, String batchNo) {
        LOGGER.info("发工资参数：" + JSON.toJSONString(customerSalarysBean));
        //获取员工信息  账户性质 1-个人 2-企业
        SubAccountBean subAccountBean = subAccountService.selectByCustId(customerSalarysBean.getSalaryCustomerId(), 1);
        if (subAccountBean != null) {
            //给企业员工发工资
            LOGGER.info("用户充值ID：" + customerSalarysBean.getSalaryCustomerId() + "   " + JSON.toJSONString(customerSalarysBean));
            subAccountService.rechargeRecords(customerSalarysBean.getSalaryCustomerId(), customerSalarysBean.getSalaryActual(), RecordSourceConstant.RECORD_SOURCE_3, customerSalarysBean.getSalaryId(), AccountType.PEOPLE);
            //更新工资单发放状态  工资是否已发放  0:未发放  1:已发放
            customerSalarysBean.setIssue(1);
            customerSalarysService.updateByPrimaryKeySelective(customerSalarysBean);
            //判断员工是否设置自动提现  是否自动转入 0:非自动 1;自动
            LOGGER.info("是否自动提现:" + subAccountBean.getIsAutoTransfer().intValue());
            if (subAccountBean.getIsAutoTransfer().intValue() == 1) {
                CustomerRechargesBean customerRechargesBean = new CustomerRechargesBean();
                //用户id
                customerRechargesBean.setRechargeCustomerId(customerSalarysBean.getSalaryCustomerId());
                //批次号
                customerRechargesBean.setBatchNumber(batchNo);
                //操作类型 类型 1充值 2提现 3发工资
                customerRechargesBean.setRechargeType(CompanyRechargeConstant.WITHDRAW_TYPE);
                //第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
                customerRechargesBean.setRechargeStation(Integer.valueOf(2));
                //注备
                customerRechargesBean.setRechargeBak("自动提现");
                //提现金额
                customerRechargesBean.setRechargeMoney(customerSalarysBean.getSalaryActual());
                //来源id
                customerRechargesBean.setResourceId(customerSalarysBean.getSalaryId());
                LOGGER.info("提现申请:" + JSON.toJSONString(customerRechargesBean));
                //发起提现申请
                customerRechargesService.customerWithdrawals(customerRechargesBean);
            }
        } else {
            LOGGER.info("用户ID：" + customerSalarysBean.getSalaryCustomerId() + "账户不存在，无法进行工资发放");
        }
    }

}
