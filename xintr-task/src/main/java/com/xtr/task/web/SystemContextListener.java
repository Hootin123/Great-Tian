package com.xtr.task.web;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import com.xtr.api.service.task.TaskScheduleJobService;
import com.xtr.comm.util.SpringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;

/**
 * 系统启动加载类。所有需要初始化的数据请放在这里。
 *
 * @author 张峰
 */
public class SystemContextListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        SpringUtils.setApplicationContext((WebApplicationContext) sce.getServletContext().
                getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
