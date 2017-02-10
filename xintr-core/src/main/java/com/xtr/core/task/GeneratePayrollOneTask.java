package com.xtr.core.task;

import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.comm.basic.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>生成工资单任务</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 13:42
 */
@Service("generatePayrollOneTask")
public class GeneratePayrollOneTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePayrollOneTask.class);

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private PayrollAccountService payrollAccountService;
    /**
     * 创建线程池
     */
    ExecutorService pool = null;


    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        LOGGER.info("开始执行生成工资单任务···");
        //获取未生成工资单的计薪周期
        List<PayCycleBean> list = payCycleService.getPayCycleBy();
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            LOGGER.info("需要生成工资单的数量【" + size + "】···");
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
            for (final PayCycleBean payCycleBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //生成工资单
                            payrollAccountService.generatePayroll(payCycleBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("payCycleId【" + payCycleBean.getId() + "】" + e.getMessage(), e);
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
        LOGGER.info("结束执行生成工资单任务···");
    }


}
