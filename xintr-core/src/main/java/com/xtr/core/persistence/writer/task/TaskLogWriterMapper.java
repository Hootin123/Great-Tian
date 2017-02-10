package com.xtr.core.persistence.writer.task;

import com.xtr.api.domain.task.TaskLogBean;

public interface TaskLogWriterMapper {
    /**
     *  根据主键删除数据库的记录,task_log
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,task_log
     *
     * @param record
     */
    int insert(TaskLogBean record);

    /**
     *  动态字段,写入数据库记录,task_log
     *
     * @param record
     */
    int insertSelective(TaskLogBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,task_log
     *
     * @param record
     */
    int updateByPrimaryKeySelective(TaskLogBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,task_log
     *
     * @param record
     */
    int updateByPrimaryKey(TaskLogBean record);
}