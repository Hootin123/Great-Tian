//package com.xtr.comm.task;
//
//import org.quartz.*;
//import org.quartz.impl.StdSchedulerFactory;
//
//
///**
// * <p>定时任务管理类</p>
// *
// * @author 张峰 zfvip_it@163.com
// * @createTime: 2016/6/17 10:07
// */
//public class QuartzManager {
//    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
//    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
//    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
//
//    /**
//     * @param jobName 任务名
//     * @param cls     任务
//     * @param time    时间设置，参考quartz说明文档
//     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
//     */
//    public static void addJob(String jobName, Class cls, String time) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类
//            // 触发器
//            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
//            trigger.setCronExpression(time);// 触发器时间设定
//            sched.scheduleJob(jobDetail, trigger);
//            // 启动
//            if (!sched.isShutdown()) {
//                sched.start();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @param jobName          任务名
//     * @param jobGroupName     任务组名
//     * @param triggerName      触发器名
//     * @param triggerGroupName 触发器组名
//     * @param jobClass         任务
//     * @param time             时间设置，参考quartz说明文档
//     * @Description: 添加一个定时任务
//     */
//    public static void addJob(String jobName, String jobGroupName,
//                              String triggerName, String triggerGroupName, Class jobClass,
//                              String time) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
//            // 触发器
//            CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
//            trigger.setCronExpression(time);// 触发器时间设定
//            sched.scheduleJob(jobDetail, trigger);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @param jobName
//     * @param time
//     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
//     */
//    public static void modifyJobTime(String jobName, String time) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
//            if (trigger == null) {
//                return;
//            }
//            String oldTime = trigger.getCronExpression();
//            if (!oldTime.equalsIgnoreCase(time)) {
//                JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
//                Class objJobClass = jobDetail.getJobClass();
//                removeJob(jobName);
//                addJob(jobName, objJobClass, time);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @param triggerName
//     * @param triggerGroupName
//     * @param time
//     * @Description: 修改一个任务的触发时间
//     */
//    public static void modifyJobTime(String triggerName,
//                                     String triggerGroupName, String time) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
//            if (trigger == null) {
//                return;
//            }
//            String oldTime = trigger.getCronExpression();
//            if (!oldTime.equalsIgnoreCase(time)) {
//                CronTrigger ct = (CronTrigger) trigger;
//                // 修改时间
//                ct.setCronExpression(time);
//                // 重启触发器
//                sched.resumeTrigger(triggerName, triggerGroupName);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @param jobName
//     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
//     */
//    public static void removeJob(String jobName) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
//            sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
//            sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @param jobName
//     * @param jobGroupName
//     * @param triggerName
//     * @param triggerGroupName
//     * @Description: 移除一个任务
//     */
//    public static void removeJob(String jobName, String jobGroupName,
//                                 String triggerName, String triggerGroupName) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
//            sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
//            sched.deleteJob(jobName, jobGroupName);// 删除任务
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 停止
//     *
//     * @param triggerName
//     * @param group
//     */
//    public void pauseTrigger(String triggerName, String group) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            sched.pauseTrigger(triggerName, group);//停止触发器
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    /**
//     * 恢复、重启
//     *
//     * @param triggerName
//     * @param group
//     */
//    public void resumeTrigger(String triggerName, String group) {
//        try {
//            //Trigger trigger = scheduler.getTrigger(triggerName, group);
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            sched.resumeTrigger(triggerName, group);//重启触发器
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    /**
//     * 删除
//     *
//     * @param triggerName
//     * @param group
//     * @return
//     */
//    public boolean removeTrigdger(String triggerName, String group) {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            sched.pauseTrigger(triggerName, group);//停止触发器
//            return sched.unscheduleJob(triggerName, group);//移除触发器
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description:启动所有定时任务
//     */
//    public static void startJobs() {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            sched.start();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description:关闭所有定时任务
//     */
//    public static void shutdownJobs() {
//        try {
//            Scheduler sched = gSchedulerFactory.getScheduler();
//            if (!sched.isShutdown()) {
//                sched.shutdown();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public static void main(String[] args) {
//        try {
//            String job_name = "动态任务调度";
//            System.out.println("【系统启动】开始(每1秒输出一次)...");
//            QuartzManager.addJob(job_name, QuartzJob.class, "0/1 * * * * ?");
//
//            Thread.sleep(5000);
//            System.out.println("【修改时间】开始(每2秒输出一次)...");
//            QuartzManager.modifyJobTime(job_name, "10/2 * * * * ?");
//            Thread.sleep(6000000);
////            System.out.println("【移除定时】开始...");
////            QuartzManager.removeJob(job_name);
////            System.out.println("【移除定时】成功");
////
////            System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
////            QuartzManager.addJob(job_name, QuartzJob.class, "*/10 * * * * ?");
////            Thread.sleep(60000);
//            System.out.println("【移除定时】开始...");
//            QuartzManager.removeJob(job_name);
//            System.out.println("【移除定时】成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
