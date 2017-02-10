package com.xtr.manager.controller.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysLogBean;
import com.xtr.api.domain.task.TaskLogBean;
import com.xtr.api.service.sys.SysLogService;
import com.xtr.api.service.task.TaskLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/8 12:00
 */
@Controller
@RequestMapping("sysLog")
public class LogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Resource
    private TaskLogService taskLogService;

    @Resource
    private SysLogService sysLogService;

    /**
     * 日志页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("log.htm")
    public ModelAndView log(ModelAndView mav) {
        mav.setViewName("xtr/sys/log/log");
        return mav;
    }

    /**
     * 任务日志
     *
     * @return
     */
    @RequestMapping("taskData.htm")
    @ResponseBody
    public ResultResponse taskData(TaskLogBean taskLogBean) {
        return taskLogService.selectPageList(taskLogBean);
    }

    /**
     * 操作日志
     *
     * @param sysLogBean
     * @return
     */
    @RequestMapping("operationData.htm")
    @ResponseBody
    public ResultResponse operationData(SysLogBean sysLogBean) {
        //日志类型 0：操作日志 1：异常日志
        sysLogBean.setType(Integer.valueOf(0));
        return sysLogService.selectPageList(sysLogBean);
    }

    /**
     * 异常日志
     *
     * @param sysLogBean
     * @return
     */
    @RequestMapping("exceptionData.htm")
    @ResponseBody
    public ResultResponse exceptionData(SysLogBean sysLogBean) {
        //日志类型 0：操作日志 1：异常日志
        sysLogBean.setType(Integer.valueOf(1));
        return sysLogService.selectPageList(sysLogBean);
    }

    /**
     * 删除任务日志
     *
     * @param id
     * @return
     */
    @RequestMapping("delete.htm")
    @ResponseBody
    public ResultResponse delete(@RequestParam("id") Long[] id) {
        return taskLogService.deleteByIds(id);
    }
}
