package com.xtr.manager.controller.task;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.task.TaskScheduleJobBean;
import com.xtr.api.service.task.TaskScheduleJobService;
import com.xtr.comm.annotation.SystemControllerLog;
import org.quartz.JobPersistenceException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/28 17:43
 */
@Controller
@RequestMapping("sysTask")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Resource
    private TaskScheduleJobService taskScheduleJobService;

    /**
     * 任务管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("task.htm")
    public ModelAndView task(ModelAndView mav) {
        mav.setViewName("xtr/sys/task/task");
        return mav;
    }

    /**
     * 新增页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("add.htm")
    public ModelAndView add(ModelAndView mav, Long id) {
        mav.setViewName("xtr/sys/task/add");
        if (id != null) {
            mav.addObject("task", taskScheduleJobService.selectByPrimaryKey(id));
        }
        return mav;
    }

    /**
     * @param taskScheduleJobBean
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(TaskScheduleJobBean taskScheduleJobBean) {
        return taskScheduleJobService.selectPageList(taskScheduleJobBean);
    }

    /**
     * 保存任务
     *
     * @param taskScheduleJobBean
     * @return
     */
    @RequestMapping("save.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增/修改任务", modelName = "任务管理")
    public ResultResponse save(TaskScheduleJobBean taskScheduleJobBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (taskScheduleJobBean.getJobId() != null) {
                taskScheduleJobService.updateJobState(taskScheduleJobBean);
            } else {
                taskScheduleJobService.addTask(taskScheduleJobBean);
            }
            resultResponse.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 删除任务
     *
     * @return
     */
    @RequestMapping("delete.htm")
    @ResponseBody
    @SystemControllerLog(operation = "删除任务", modelName = "任务管理")
    public ResultResponse delete(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        int result = taskScheduleJobService.deleteByPrimaryKey(id);
        if (result > 0) {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 启动任务
     *
     * @param jobId
     * @return
     */
    @RequestMapping("startJob.htm")
    @ResponseBody
    @SystemControllerLog(operation = "启动任务", modelName = "任务管理")
    public ResultResponse startJob(Long jobId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            //获取任务信息
            TaskScheduleJobBean taskScheduleJobBean = taskScheduleJobService.selectByPrimaryKey(jobId);
            if (taskScheduleJobBean != null) {
                taskScheduleJobBean.setJobStatus(TaskScheduleJobBean.STATUS_RUNNING);
                //更新任务状态
                taskScheduleJobService.updateByPrimaryKeySelective(taskScheduleJobBean);
                //启动任务
                taskScheduleJobService.addJob(taskScheduleJobBean);
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("任务不存在");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("启动任务失败");
        }
        return resultResponse;
    }

    /**
     * 立即执行job
     *
     * @param jobId
     * @return
     */
    @RequestMapping("runAJobNow.htm")
    @ResponseBody
    @SystemControllerLog(operation = "立即运行任务", modelName = "任务管理")
    public ResultResponse runAJobNow(Long jobId) {
        ResultResponse resultResponse = new ResultResponse();
        TaskScheduleJobBean taskScheduleJobBean = null;
        try {
            //获取任务信息
            taskScheduleJobBean = taskScheduleJobService.selectByPrimaryKey(jobId);
            if (taskScheduleJobBean != null) {
                taskScheduleJobService.runAJobNow(taskScheduleJobBean);
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("任务不存在");
            }
        } catch (JobPersistenceException e) {
            //说明任务不在列表中
            try {
                if(taskScheduleJobBean != null) {
                    taskScheduleJobBean.setJobStatus(TaskScheduleJobBean.STATUS_STOP);
                    //添加任务
                    taskScheduleJobService.addJob(taskScheduleJobBean);
                    //立即执行
                    taskScheduleJobService.runAJobNow(taskScheduleJobBean);
                }
                resultResponse.setSuccess(true);
            } catch (SchedulerException e1) {
                LOGGER.error(e.getMessage(), e);
                resultResponse.setMessage("启动任务失败");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("立即执行失败");
        }
        return resultResponse;
    }

    /**
     * 暂停job
     *
     * @param jobId
     * @return
     */
    @RequestMapping("pauseJob.htm")
    @ResponseBody
    @SystemControllerLog(operation = "暂停任务", modelName = "任务管理")
    public ResultResponse pauseJob(Long jobId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            //获取任务信息
            TaskScheduleJobBean taskScheduleJobBean = taskScheduleJobService.selectByPrimaryKey(jobId);
            if (taskScheduleJobBean != null) {
                taskScheduleJobService.pauseJob(taskScheduleJobBean);
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("任务不存在");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("立即执行失败");
        }
        return resultResponse;
    }

    /**
     * 恢复job
     *
     * @param jobId
     * @return
     */
    @RequestMapping("resumeJob.htm")
    @ResponseBody
    @SystemControllerLog(operation = "恢复任务", modelName = "任务管理")
    public ResultResponse resumeJob(Long jobId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            //获取任务信息
            TaskScheduleJobBean taskScheduleJobBean = taskScheduleJobService.selectByPrimaryKey(jobId);
            if (taskScheduleJobBean != null) {
                taskScheduleJobService.resumeJob(taskScheduleJobBean);
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("任务不存在");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("恢复任务失败");
        }
        return resultResponse;
    }

    /**
     * 删除job  从任务列表中删除 不删除数据库
     *
     * @param jobId
     * @return
     */
    @RequestMapping("deleteJob.htm")
    @ResponseBody
    @SystemControllerLog(operation = "移除任务队列", modelName = "任务管理")
    public ResultResponse deleteJob(Long jobId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            //获取任务信息
            TaskScheduleJobBean taskScheduleJobBean = taskScheduleJobService.selectByPrimaryKey(jobId);
            if (taskScheduleJobBean != null) {
                taskScheduleJobService.deleteJob(taskScheduleJobBean);
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("任务不存在");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("删除任务失败");
        }
        return resultResponse;
    }
}
