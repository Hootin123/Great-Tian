package com.xtr.core.task;

import com.xtr.api.domain.task.TaskLogBean;
import com.xtr.api.domain.task.TaskScheduleJobBean;
import com.xtr.api.service.task.TaskLogService;
import com.xtr.api.service.task.TaskService;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/5 18:31
 */
public abstract class BaseTask implements TaskService {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseTask.class);

    @Resource
    private TaskLogService taskLogService;

    /**
     * 执行任务
     */
    public void execute(TaskScheduleJobBean scheduleJob) {
        Long id = 0l;
        try {
            //任务执行前处理
            id = beforeTask(scheduleJob);
            run();
            //任务执行后处理
            afterTask(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //异常任务处理
            failTask(id, StringUtils.getStackTrace(e));
        }
    }

    public abstract void run() throws Exception;

    /**
     * 任务执行前处理
     *
     * @return
     */
    private Long beforeTask(TaskScheduleJobBean scheduleJob) throws UnknownHostException {
        //*========数据库日志=========*//
        TaskLogBean taskLogBean = new TaskLogBean();
        //任务描述
        taskLogBean.setTaskName(scheduleJob.getJobName());
        //任务Id
        taskLogBean.setTaskId(scheduleJob.getSpringId());
        //任务开始时间
        taskLogBean.setStartTime(new Date());
        //服务器名称
        taskLogBean.setServerName(System.getProperties().getProperty("os.name"));
        //服务器IP
        taskLogBean.setServerIp(InetAddress.getLocalHost().getHostAddress());
        //任务状态  0：成功 1：失败
        taskLogBean.setState(Integer.valueOf(0));
        //任务类型
        taskLogBean.setType(Integer.valueOf(0));
        //创建时间
        taskLogBean.setCreateTime(new Date());

        //插入数据库
        taskLogService.insert(taskLogBean);
        return taskLogBean.getId();
    }

    /**
     * 任务执行前处理
     */
    private void afterTask(Long id) {
        //*========数据库日志=========*//
        //获取task日志
        TaskLogBean taskLogBean = taskLogService.selectByPrimaryKey(id);
        //结束时间
        taskLogBean.setEndTime(new Date());
        //耗时
        taskLogBean.setTookTime(DateUtil.dateDiff(taskLogBean.getStartTime().getTime(), taskLogBean.getEndTime().getTime()));
        taskLogService.updateByPrimaryKeySelective(taskLogBean);
    }

    /**
     * 任务失败处理
     *
     * @param id
     * @param msg
     */
    private void failTask(Long id, String msg) {
        //*========数据库日志=========*//
        //获取task日志
        TaskLogBean taskLogBean = taskLogService.selectByPrimaryKey(id);
        //结束时间
        taskLogBean.setEndTime(new Date());
        //说明
        taskLogBean.setRemarks(msg.length() > 2048 ? msg.substring(0, 2047) : msg);
        //任务状态 0:成功 1:失败
        taskLogBean.setState(Integer.valueOf(1));
        //耗时
        taskLogBean.setTookTime(DateUtil.dateDiff(taskLogBean.getStartTime().getTime(), taskLogBean.getEndTime().getTime()));
        taskLogService.updateByPrimaryKeySelective(taskLogBean);
    }
}
