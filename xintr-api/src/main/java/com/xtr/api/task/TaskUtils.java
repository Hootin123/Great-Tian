package com.xtr.api.task;


import com.xtr.api.domain.task.TaskScheduleJobBean;
import com.xtr.api.service.task.TaskService;
import com.xtr.comm.util.SpringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Task工具类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/3 12:13
 */
public class TaskUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskUtils.class);

    /**
     * 通过反射调用scheduleJob中定义的方法
     *
     * @param scheduleJob
     */
    public static void invokMethod(TaskScheduleJobBean scheduleJob) {
        Object object = null;
        TaskService taskService = null;
        Class clazz = null;
        if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
            taskService = (TaskService) SpringUtils.getBean(scheduleJob.getSpringId());
            //执行任务
            try {
                taskService.execute(scheduleJob);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
                try {
                    clazz = Class.forName(scheduleJob.getBeanClass());
                    object = clazz.newInstance();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }

            }
            if (object == null) {
                LOGGER.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
                return;
            }
            clazz = object.getClass();
            Method method = null;
            try {
                method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
            } catch (NoSuchMethodException e) {
                LOGGER.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
            } catch (SecurityException e) {
                LOGGER.error(e.getMessage(), e);
            }
            if (method != null) {
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (IllegalArgumentException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        LOGGER.info("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
    }
}
