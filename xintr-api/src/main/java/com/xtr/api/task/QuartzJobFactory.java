package com.xtr.api.task;


import com.xtr.api.domain.task.TaskScheduleJobBean;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * <p>计划任务执行处 无状态</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/3 12:13
 */
public class QuartzJobFactory implements Job {
    public final Logger log = Logger.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TaskScheduleJobBean scheduleJob = (TaskScheduleJobBean) context.getMergedJobDataMap().get("TaskScheduleJobBean");
        TaskUtils.invokMethod(scheduleJob);
    }
}