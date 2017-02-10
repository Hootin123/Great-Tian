package com.xtr.task;

import com.xtr.comm.task.QuartzManager;
import com.xtr.task.task.PayDayTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 17:41
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationcontext.xml")
public class TaskTest {

//    @Resource
//    private TaskLogService taskLogService;

    @Test
    public void testTaskLog() {
//        taskLogService.getRunningSuccessedById("ceshi");

//        TaskLogBean taskLogBean = new TaskLogBean();
//        //task标识
//        taskLogBean.setTaskId("payDayTask" + DateUtil.formatCurrentDate());
////        Long currentRecordNum = taskLogService.getSerialNo(taskLogBean.getTaskId());
//        //序列
//        taskLogBean.setSerialNo(currentRecordNum + 1);
//        //task名称
//        taskLogBean.setTaskName("payDayTask");
//        //所属job 	???
////        taskLogBean.setJobId("JobId");
//        //task描述
//        taskLogBean.setRemark("测试");
//        //task状态
//        taskLogBean.setStatus(0);
//        //task开始时间
//        taskLogBean.setStartTime(new Date());
//        //操作员ID
//        taskLogBean.setUserId(0l);
//        //创建时间
//        taskLogBean.setCreateTime(new Date());

//        ResultResponse resultResponse = taskLogService.insert(taskLogBean);
//        System.out.println(JSON.toJSONString(resultResponse));
    }

    public static void main(String args[]) throws InterruptedException {
        String job_name = "动态任务调度";
        System.out.println("【系统启动】开始(每1秒输出一次)...");
        QuartzManager.addJob(job_name, PayDayTask.class, "0 0/1 * * * ?");
//
//        Thread.sleep(3000);
//        System.out.println("【移除定时】开始...");
//        QuartzManager.removeJob(job_name);
//        System.out.println("【移除定时】成功");
    }
}
