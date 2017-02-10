package com.xtr.core.task;

import com.xtr.core.persistence.writer.customer.CustomerExpenseDetailWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 更新定额状态
 * @Author Xuewu
 * @Date 2016/9/8.
 */
@Service("updateExpenseStatusTask")
public class UpdateExpenseStatusTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateExpenseStatusTask.class);

    @Resource
    private CustomerExpenseDetailWriterMapper customerExpenseDetailWriterMapper;


    @Override
    public void run() throws Exception {
        LOGGER.info("开始执行 更新员工定额状态 任务");
        int i = customerExpenseDetailWriterMapper.updateExpenseStatus();
        LOGGER.info("更新 " + i +" 人定额状态！");
        LOGGER.info("更新员工定额状态 任务 执行完毕");
    }
}
