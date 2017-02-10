package com.xtr.api.service.task;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.task.TaskLogBean;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/5 12:01
 */

public interface TaskLogService {


    /**
     * 新写入数据库记录,task_log
     *
     * @param record
     */
    int insert(TaskLogBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,task_log
     *
     * @param record
     */
    int updateByPrimaryKeySelective(TaskLogBean record);

    /**
     * 根据任务Id查询最新的任务
     *
     * @param taskId
     * @return
     */
    TaskLogBean selectByTaskIdLast(String taskId);

    /**
     * 根据指定主键获取一条数据库记录,task_log
     *
     * @param id
     */
    TaskLogBean selectByPrimaryKey(Long id);

    /**
     * 分页查询任务日志
     *
     * @param taskLogBean
     * @return
     */
    ResultResponse selectPageList(TaskLogBean taskLogBean);

    /**
     * 批量删除任务日志
     *
     * @param ids
     * @return
     */
    ResultResponse deleteByIds(Long[] ids);
}
