package com.xtr.core.persistence.reader.task;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.task.TaskScheduleJobBean;

import java.util.List;

public interface TaskScheduleJobReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,task_schedule_job
     *
     * @param jobId
     */
    TaskScheduleJobBean selectByPrimaryKey(Long jobId);

    /**
     * 分页查询任务列表
     *
     * @return
     */
    PageList selectPageList(PageBounds pageBounds);

    /**
     * 从数据库中获取所有状态为执行中的任务
     *
     * @return
     */
    List<TaskScheduleJobBean> queryAll();

}