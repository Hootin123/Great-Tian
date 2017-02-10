package com.xtr.core.task;

import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.customer.CustomersService;
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
 * <p>生成员工账户任务</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/31 10:34
 */
@Service("generateSubAccount")
public class GenerateSubAccount extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateSubAccount.class);

    @Resource
    private CustomersService customersService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        LOGGER.info("开始执行生成员工账户任务···");
        //获取未生成账户的员工id
        List<CustomersBean> list = customersService.selectCustoemrSubAccount();
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            LOGGER.info("需要生成账户的员工数量【" + size + "】");
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);

            for (final CustomersBean customersBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //生成账户
                            customersService.addSubAccount(customersBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("customerId【" + customersBean.getCustomerId() + "】出现异常···" + e.getMessage(), e);
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
        LOGGER.info("生成员工账户任务执行结束···");
    }
}
