package com.xtr.core.persistence.reader.task;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.task.TaskLogBean;

public interface TaskLogReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,task_log
     *
     * @param id
     */
    TaskLogBean selectByPrimaryKey(Long id);

    /**
     * 根据任务Id查询最新的任务
     *
     * @param taskId
     * @return
     */
    TaskLogBean selectByTaskIdLast(String taskId);

    /**
     * 分页查询任务日志
     *
     * @param taskLogBean
     * @param pageBounds
     * @return
     */
    PageList<TaskLogBean> listPage(TaskLogBean taskLogBean, PageBounds pageBounds);

}