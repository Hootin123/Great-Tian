package com.xtr.comm.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p>任务执行类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:07
 */
public class QuartzJob implements Job {

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
//        System.out.println(arg0.getJobDetail().getName());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "★★★★★★★★★★★");
    }
}
