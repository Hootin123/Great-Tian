package com.xtr.task.service.task;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.task.TaskScheduleJobBean;
import com.xtr.api.service.task.TaskScheduleJobService;
import com.xtr.api.task.QuartzJobFactory;
import com.xtr.api.task.QuartzJobFactoryDisallowConcurrentExecution;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.StringUtils;
import com.xtr.task.persistence.reader.task.TaskScheduleJobReaderMapper;
import com.xtr.task.persistence.writer.task.TaskScheduleJobWriterMapper;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>定时任务实现类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/28 16:44
 */
@Service("taskScheduleJobService")
public class TaskScheduleJobServiceImpl implements TaskScheduleJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduleJobServiceImpl.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Resource
    private TaskScheduleJobReaderMapper taskScheduleJobReaderMapper;

    @Resource
    private TaskScheduleJobWriterMapper taskScheduleJobWriterMapper;

    /**
     * 初始化Task
     */
    public void initTask() {
        List<TaskScheduleJobBean> list = queryAll();
        if (!list.isEmpty()) {
            for (TaskScheduleJobBean taskScheduleJobBean : list) {
                try {
                    addJob(taskScheduleJobBean);
                } catch (SchedulerException e) {
                    LOGGER.error("初始化Task失败", e);
                }
            }
        }
    }

    /**
     * 从数据库中取 区别于getAllJob
     *
     * @return
     */
    public ResultResponse selectPageList(TaskScheduleJobBean taskScheduleJobBean) {
        ResultResponse resultResponse = new ResultResponse();
        Pager pageBounds = new Pager(taskScheduleJobBean.getPageIndex(), taskScheduleJobBean.getPageSize());
        PageList list = taskScheduleJobReaderMapper.selectPageList(pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 从数据库中获取所有状态为执行中的任务
     *
     * @return
     */
    public List<TaskScheduleJobBean> queryAll() {
        return taskScheduleJobReaderMapper.queryAll();
    }

    /**
     * 添加到数据库中 区别于addJob
     */
    public void addTask(TaskScheduleJobBean job) {
        //job格式验证
        checkJob(job);
        job.setCreateTime(new Date());
        job.setJobStatus(TaskScheduleJobBean.STATUS_PLAN);//默认计划中
        taskScheduleJobWriterMapper.insertSelective(job);
    }

    /**
     * job格式验证
     *
     * @param job
     */
    private void checkJob(TaskScheduleJobBean job) {
        //cron表达式验证
        try {
            CronExpression exp = new CronExpression(job.getCronExpression());
        } catch (ParseException e) {
            throw new BusinessException("cron表达式错误");
        }
        //验证类路径
        if (StringUtils.isChinese(job.getBeanClass())) {
            throw new BusinessException("类路径格式非法");
        }
        //验证方法名
        if (StringUtils.isChinese(job.getMethodName())) {
            throw new BusinessException("方法名格式非法");
        }
    }

    /**
     * 从数据库中查询job
     */
    public TaskScheduleJobBean selectByPrimaryKey(Long jobId) {
        return taskScheduleJobReaderMapper.selectByPrimaryKey(jobId);
    }

    /**
     *  根据主键删除数据库的记录,task_schedule_job
     *
     * @param jobId
     */
    public int deleteByPrimaryKey(Long jobId){
        return taskScheduleJobWriterMapper.deleteByPrimaryKey(jobId);
    }

    /**
     * 更改任务状态
     *
     * @throws SchedulerException
     */
    public void changeStatus(Long jobId, String jobStatus) throws SchedulerException {
        TaskScheduleJobBean job = selectByPrimaryKey(jobId);
        if (job == null) {
            return;
        }
        if (TaskScheduleJobBean.STATUS_STOP.equals(jobStatus)) {//暂停
            deleteJob(job);
            job.setJobStatus(TaskScheduleJobBean.STATUS_STOP);
        } else if (TaskScheduleJobBean.STATUS_RUNNING.equals(jobStatus)) {//启动
            job.setJobStatus(TaskScheduleJobBean.STATUS_RUNNING);
            addJob(job);
        }
        taskScheduleJobWriterMapper.updateByPrimaryKeySelective(job);
    }


    /**
     * 更新任务  不更新数据库
     *
     * @throws SchedulerException
     */
    public void updateJobState(TaskScheduleJobBean job) throws SchedulerException {
        //job格式验证
        checkJob(job);
        if (TaskScheduleJobBean.STATUS_RUNNING.equals(job.getJobStatus())) {
            updateJobCron(job);
        }
        taskScheduleJobWriterMapper.updateByPrimaryKeySelective(job);
    }

    /**
     * 更新任务  只更新数据库
     *
     * @throws SchedulerException
     */
    public void updateByPrimaryKeySelective(TaskScheduleJobBean job) throws SchedulerException {
        taskScheduleJobWriterMapper.updateByPrimaryKeySelective(job);
    }

    /**
     * 添加任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void addJob(TaskScheduleJobBean job) throws SchedulerException {
        if (job == null || (!TaskScheduleJobBean.STATUS_RUNNING.equals(job.getJobStatus()) && (!TaskScheduleJobBean.STATUS_STOP.equals(job.getJobStatus())))) {
            return;
        }

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        LOGGER.debug(scheduler + ".......................................................................................add");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = TaskScheduleJobBean.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

            jobDetail.getJobDataMap().put("TaskScheduleJobBean", job);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }

        if (TaskScheduleJobBean.STATUS_STOP.equals(job.getJobStatus())) {
            //暂停任务
//            pauseJob(job);
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
            scheduler.pauseJob(jobKey);
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<TaskScheduleJobBean> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<TaskScheduleJobBean> jobList = new ArrayList<TaskScheduleJobBean>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                TaskScheduleJobBean job = new TaskScheduleJobBean();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<TaskScheduleJobBean> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<TaskScheduleJobBean> jobList = new ArrayList<TaskScheduleJobBean>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            TaskScheduleJobBean job = new TaskScheduleJobBean();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    public void pauseJob(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskScheduleJobBean.getJobName(), taskScheduleJobBean.getJobGroup());
        scheduler.pauseJob(jobKey);
        taskScheduleJobBean.setJobStatus(TaskScheduleJobBean.STATUS_STOP);
        taskScheduleJobWriterMapper.updateByPrimaryKeySelective(taskScheduleJobBean);
    }

    /**
     * 恢复一个job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    public void resumeJob(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskScheduleJobBean.getJobName(), taskScheduleJobBean.getJobGroup());
        scheduler.resumeJob(jobKey);
        taskScheduleJobBean.setJobStatus(TaskScheduleJobBean.STATUS_RUNNING);
        taskScheduleJobWriterMapper.updateByPrimaryKeySelective(taskScheduleJobBean);
    }

    /**
     * 删除一个job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    public void deleteJob(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskScheduleJobBean.getJobName(), taskScheduleJobBean.getJobGroup());
        scheduler.deleteJob(jobKey);
        taskScheduleJobBean.setJobStatus(TaskScheduleJobBean.STATUS_PLAN);
        taskScheduleJobWriterMapper.updateByPrimaryKeySelective(taskScheduleJobBean);
    }

    /**
     * 立即执行job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    public void runAJobNow(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(taskScheduleJobBean.getJobName(), taskScheduleJobBean.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    public void updateJobCron(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey(taskScheduleJobBean.getJobName(), taskScheduleJobBean.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(taskScheduleJobBean.getCronExpression());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}
