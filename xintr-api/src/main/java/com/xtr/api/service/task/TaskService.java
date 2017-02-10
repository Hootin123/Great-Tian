package com.xtr.api.service.task;

import com.xtr.api.domain.task.TaskScheduleJobBean;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/5 18:10
 */

public interface TaskService {

    /**
     * 执行任务
     */
    void execute(TaskScheduleJobBean scheduleJob);
}
