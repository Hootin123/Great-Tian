package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.RecordSourceConstant;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import com.xtr.core.persistence.writer.salary.PayCycleWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
 * <p>发工资任务</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/26 9:57
 */
@Service("payDayOneTask")
public class PayDayOneTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayDayOneTask.class);

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private PayCycleWriterMapper payCycleWriterMapper;

    @Resource
    private CustomerPayrollReaderMapper customerPayrollReaderMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

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
        LOGGER.info("开始执行发工资任务···");
        //获取工资发放状态为带发放的计薪周期  0:未发放 1:待发放  2:发放中  3:已发放
        List<PayCycleBean> list = payCycleReaderMapper.selectPayCycleSalary();
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            LOGGER.info("需要发工资的数量【" + size + "】");
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<>();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            for (final PayCycleBean payCycleBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //工资发放
                            payOff(payCycleBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("payCycleId【" + payCycleBean.getId() + "】出现异常···" + e.getMessage(), e);
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
        LOGGER.info("发工资任务执行结束···");
    }

    /**
     * 工资发放
     *
     * @param payCycleBean
     */
    @Transactional
    private void payOff(PayCycleBean payCycleBean) {
        //获取计薪周期对应的工资单
        List<CustomerPayrollBean> list = customerPayrollReaderMapper.selectPayrollByPayCycleId(payCycleBean.getId());
        if (list != null && !list.isEmpty()) {
            //更新计薪周期工资发放状态  当月工资是否已发放 0:未发放 1:待发放  2:发放中  3:已发放
            payCycleBean.setIsPayOff(2);
            String batchNo = UUID.randomUUID().toString();
            for (CustomerPayrollBean customerPayrollBean : list) {
                try {
                    payday(customerPayrollBean, batchNo);
                } catch (Exception e) {
                    LOGGER.error("customerPayrollId【" + customerPayrollBean.getId() + "】工资单发放失败···");
                }
            }
        } else {
            //更新计薪周期工资发放状态  当月工资是否已发放 0:未发放 1:待发放  2:发放中  3:已发放
            payCycleBean.setIsPayOff(3);
        }
        payCycleWriterMapper.updateByPrimaryKeySelective(payCycleBean);
    }

    /**
     * 发工资
     *
     * @param customerPayrollBean
     * @param batchNo
     */
    private void payday(CustomerPayrollBean customerPayrollBean, String batchNo) {
        LOGGER.info("发工资参数：" + JSON.toJSONString(customerPayrollBean));
        //获取员工信息  账户性质 1-个人 2-企业
        SubAccountBean subAccountBean = subAccountService.selectByCustId(customerPayrollBean.getCustomerId(), 1);
        if (subAccountBean != null) {
            if (customerPayrollBean.getRealWage().doubleValue() > 0) {
                //给企业员工发工资
                LOGGER.info("用户充值ID：" + customerPayrollBean.getCustomerId() + "   " + JSON.toJSONString(customerPayrollBean));
                subAccountService.rechargeRecords(customerPayrollBean.getCustomerId(), customerPayrollBean.getRealWage(), RecordSourceConstant.RECORD_SOURCE_3, customerPayrollBean.getId(), AccountType.PEOPLE);
                //判断员工是否设置自动提现  是否自动转入 0:非自动 1;自动
                LOGGER.info("是否自动提现:" + subAccountBean.getIsAutoTransfer().intValue());
                if (subAccountBean.getIsAutoTransfer().intValue() == 1) {
                    CustomerRechargesBean customerRechargesBean = new CustomerRechargesBean();
                    //用户id
                    customerRechargesBean.setRechargeCustomerId(customerPayrollBean.getCustomerId());
                    //批次号
                    customerRechargesBean.setBatchNumber(batchNo);
                    //操作类型 类型 1充值 2提现 3发工资
                    customerRechargesBean.setRechargeType(CompanyRechargeConstant.WITHDRAW_TYPE);
                    //第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
                    customerRechargesBean.setRechargeStation(Integer.valueOf(2));
                    //注备
                    customerRechargesBean.setRechargeBak("自动提现");
                    //提现金额
                    customerRechargesBean.setRechargeMoney(customerPayrollBean.getRealWage());
                    //来源id
                    customerRechargesBean.setResourceId(customerPayrollBean.getId());
                    LOGGER.info("提现申请:" + JSON.toJSONString(customerRechargesBean));
                    //发起提现申请
                    customerRechargesService.customerWithdrawals(customerRechargesBean);
                    //解冻扣款
                    subAccountService.thawDeduct(customerRechargesBean.getRechargeCustomerId(), customerRechargesBean.getRechargeMoney(), RecordSourceConstant.RECORD_SOURCE_2, customerRechargesBean.getRechargeId(), AccountType.PEOPLE);
                }
            }
            //更新工资单发放状态  工资是否已发放  0:未发放  1:已发放
            customerPayrollBean.setIsPayOff(1);
            customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
        } else {
            LOGGER.info("用户ID：" + customerPayrollBean.getCustomerId() + "账户不存在，无法进行工资发放");
        }
    }
}
