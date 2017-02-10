package com.xtr.task.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/29 10:12
 */
@Component
public class TestTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestTask.class);

    public void run() {
        LOGGER.info("Task执行中···");
    }
}
