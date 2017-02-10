package com.xtr.api.service.task;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.task.TaskScheduleJobBean;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>定时任务接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/28 16:37
 */

public interface TaskScheduleJobService {

    /**
     * 初始化Task
     */
    void initTask();

    /**
     * 从数据库中取
     *
     * @return
     */
    ResultResponse selectPageList(TaskScheduleJobBean taskScheduleJobBean);

    /**
     * 从数据库中获取所有状态为执行中的任务
     *
     * @return
     */
    List<TaskScheduleJobBean> queryAll();

    /**
     * 添加到数据库中 区别于addJob
     */
    void addTask(TaskScheduleJobBean job);

    /**
     * 从数据库中查询job
     */
    TaskScheduleJobBean selectByPrimaryKey(Long jobId);

    /**
     *  根据主键删除数据库的记录,task_schedule_job
     *
     * @param jobId
     */
    int deleteByPrimaryKey(Long jobId);

    /**
     * 更改任务状态
     *
     * @throws SchedulerException
     */
    void changeStatus(Long jobId, String cmd) throws SchedulerException;

    /**
     * 更新任务
     *
     * @throws SchedulerException
     */
    void updateByPrimaryKeySelective(TaskScheduleJobBean job) throws SchedulerException;


    /**
     * 更新任务
     *
     * @throws SchedulerException
     */
    void updateJobState(TaskScheduleJobBean job) throws SchedulerException;

    /**
     * 添加任务
     *
     * @param job
     * @throws SchedulerException
     */
    void addJob(TaskScheduleJobBean job) throws SchedulerException;


    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    List<TaskScheduleJobBean> getAllJob() throws SchedulerException;

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    List<TaskScheduleJobBean> getRunningJob() throws SchedulerException;

    /**
     * 暂停一个job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    void pauseJob(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException;

    /**
     * 恢复一个job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    void resumeJob(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException;

    /**
     * 删除一个job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    void deleteJob(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException;

    /**
     * 立即执行job
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    void runAJobNow(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException;

    /**
     * 更新job时间表达式
     *
     * @param taskScheduleJobBean
     * @throws SchedulerException
     */
    void updateJobCron(TaskScheduleJobBean taskScheduleJobBean) throws SchedulerException;
}
