package com.xtr.core.persistence.writer.task;

import com.xtr.api.domain.task.TaskScheduleJobBean;

public interface TaskScheduleJobWriterMapper {
    /**
     *  根据主键删除数据库的记录,task_schedule_job
     *
     * @param jobId
     */
    int deleteByPrimaryKey(Long jobId);

    /**
     *  新写入数据库记录,task_schedule_job
     *
     * @param record
     */
    int insert(TaskScheduleJobBean record);

    /**
     *  动态字段,写入数据库记录,task_schedule_job
     *
     * @param record
     */
    int insertSelective(TaskScheduleJobBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,task_schedule_job
     *
     * @param record
     */
    int updateByPrimaryKeySelective(TaskScheduleJobBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,task_schedule_job
     *
     * @param record
     */
    int updateByPrimaryKey(TaskScheduleJobBean record);
}