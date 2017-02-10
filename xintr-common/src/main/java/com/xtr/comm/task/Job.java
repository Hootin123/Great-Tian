package com.xtr.comm.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 17:49
 */
public interface Job {

    /**
     * 获取任务
     *
     * @return
     */
//    AbstractTask getTask();
//
//    /**
//     * 获取任务运行监听
//     *
//     * @return
//     */
//    TaskListener getTaskListener();
//
//    /**
//     * 运行
//     */
//    void run(JobExecutionContext jobContext);

    /**
     * 执行任务
     *
     * @param job
     * @throws JobExecutionException
     */
    void execute(JobExecutionContext job) throws JobExecutionException;
}