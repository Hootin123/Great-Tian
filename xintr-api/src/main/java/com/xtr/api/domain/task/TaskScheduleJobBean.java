package com.xtr.api.domain.task;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class TaskScheduleJobBean extends BaseObject implements Serializable {


    public static final String STATUS_PLAN = "0";//计划中
    public static final String STATUS_RUNNING = "1";//运行中
    public static final String STATUS_STOP = "2";//暂停
    public static final String CONCURRENT_IS = "1";//同步运行
    public static final String CONCURRENT_NOT = "0";//不同步运行

    /**
     * ,所属表字段为task_schedule_job.job_id
     */
    private Long jobId;

    /**
     * ,所属表字段为task_schedule_job.create_time
     */
    private Date createTime;

    /**
     * ,所属表字段为task_schedule_job.update_time
     */
    private Date updateTime;

    /**
     * ,所属表字段为task_schedule_job.job_name
     */
    private String jobName;

    /**
     * ,所属表字段为task_schedule_job.job_group
     */
    private String jobGroup;

    /**
     * ,所属表字段为task_schedule_job.job_status
     */
    private String jobStatus;

    /**
     * ,所属表字段为task_schedule_job.cron_expression
     */
    private String cronExpression;

    /**
     * ,所属表字段为task_schedule_job.description
     */
    private String description;

    /**
     * ,所属表字段为task_schedule_job.bean_class
     */
    private String beanClass;

    /**
     * 1,所属表字段为task_schedule_job.is_concurrent
     */
    private String isConcurrent;

    /**
     * ,所属表字段为task_schedule_job.spring_id
     */
    private String springId;

    /**
     * ,所属表字段为task_schedule_job.method_name
     */
    private String methodName;

    /**
     * 获取  字段:task_schedule_job.job_id
     *
     * @return task_schedule_job.job_id,
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 设置  字段:task_schedule_job.job_id
     *
     * @param jobId task_schedule_job.job_id,
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取  字段:task_schedule_job.create_time
     *
     * @return task_schedule_job.create_time,
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置  字段:task_schedule_job.create_time
     *
     * @param createTime task_schedule_job.create_time,
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取  字段:task_schedule_job.update_time
     *
     * @return task_schedule_job.update_time,
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置  字段:task_schedule_job.update_time
     *
     * @param updateTime task_schedule_job.update_time,
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取  字段:task_schedule_job.job_name
     *
     * @return task_schedule_job.job_name,
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置  字段:task_schedule_job.job_name
     *
     * @param jobName task_schedule_job.job_name,
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 获取  字段:task_schedule_job.job_group
     *
     * @return task_schedule_job.job_group,
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置  字段:task_schedule_job.job_group
     *
     * @param jobGroup task_schedule_job.job_group,
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    /**
     * 获取  字段:task_schedule_job.job_status
     *
     * @return task_schedule_job.job_status,
     */
    public String getJobStatus() {
        return jobStatus;
    }

    /**
     * 设置  字段:task_schedule_job.job_status
     *
     * @param jobStatus task_schedule_job.job_status,
     */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    /**
     * 获取  字段:task_schedule_job.cron_expression
     *
     * @return task_schedule_job.cron_expression,
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置  字段:task_schedule_job.cron_expression
     *
     * @param cronExpression task_schedule_job.cron_expression,
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    /**
     * 获取  字段:task_schedule_job.description
     *
     * @return task_schedule_job.description,
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置  字段:task_schedule_job.description
     *
     * @param description task_schedule_job.description,
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取  字段:task_schedule_job.bean_class
     *
     * @return task_schedule_job.bean_class,
     */
    public String getBeanClass() {
        return beanClass;
    }

    /**
     * 设置  字段:task_schedule_job.bean_class
     *
     * @param beanClass task_schedule_job.bean_class,
     */
    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass == null ? null : beanClass.trim();
    }

    /**
     * 获取 1 字段:task_schedule_job.is_concurrent
     *
     * @return task_schedule_job.is_concurrent, 1
     */
    public String getIsConcurrent() {
        return isConcurrent;
    }

    /**
     * 设置 1 字段:task_schedule_job.is_concurrent
     *
     * @param isConcurrent task_schedule_job.is_concurrent, 1
     */
    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent == null ? null : isConcurrent.trim();
    }

    /**
     * 获取  字段:task_schedule_job.spring_id
     *
     * @return task_schedule_job.spring_id,
     */
    public String getSpringId() {
        return springId;
    }

    /**
     * 设置  字段:task_schedule_job.spring_id
     *
     * @param springId task_schedule_job.spring_id,
     */
    public void setSpringId(String springId) {
        this.springId = springId == null ? null : springId.trim();
    }

    /**
     * 获取  字段:task_schedule_job.method_name
     *
     * @return task_schedule_job.method_name,
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置  字段:task_schedule_job.method_name
     *
     * @param methodName task_schedule_job.method_name,
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }
}