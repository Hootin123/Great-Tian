package com.xtr.core.service.task;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.task.TaskLogBean;
import com.xtr.api.service.task.TaskLogService;
import com.xtr.core.persistence.reader.task.TaskLogReaderMapper;
import com.xtr.core.persistence.writer.task.TaskLogWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/5 12:02
 */
@Service("taskLogService")
public class TaskLogServiceImpl implements TaskLogService {

    @Resource
    private TaskLogReaderMapper taskLogReaderMapper;

    @Resource
    private TaskLogWriterMapper taskLogWriterMapper;


    /**
     * 新写入数据库记录,task_log
     *
     * @param record
     */
    public int insert(TaskLogBean record) {
        return taskLogWriterMapper.insert(record);
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,task_log
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(TaskLogBean record) {
        return taskLogWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据任务Id查询最新的任务
     *
     * @param taskId
     * @return
     */
    public TaskLogBean selectByTaskIdLast(String taskId) {
        return taskLogReaderMapper.selectByTaskIdLast(taskId);
    }

    /**
     * 根据指定主键获取一条数据库记录,task_log
     *
     * @param id
     */
    public TaskLogBean selectByPrimaryKey(Long id) {
        return taskLogReaderMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询任务日志
     *
     * @param taskLogBean
     * @return
     */
    public ResultResponse selectPageList(TaskLogBean taskLogBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(taskLogBean.getPageIndex(), taskLogBean.getPageSize());
        PageList<TaskLogBean> list = taskLogReaderMapper.listPage(taskLogBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 批量删除任务日志
     *
     * @param ids
     * @return
     */
    public ResultResponse deleteByIds(Long[] ids) {
        ResultResponse resultResponse = new ResultResponse();
        if (ids != null) {
            for (Long id : ids) {
                taskLogWriterMapper.deleteByPrimaryKey(id);
            }
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

}
