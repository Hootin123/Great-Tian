package com.xtr.comm.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>描述</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 13:02
 */
public class DefaultTaskListener implements TaskListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskListener.class);

    /**
     *
     */
    public DefaultTaskListener() {
    }

    /**
     * @see com.xtr.comm.task.TaskListener#beforeTaskStart(com.xtr.comm.task.TaskContext)
     */
    public void beforeTaskStart(TaskContext taskContext) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("beforeTaskStart--" + taskContext.getTask().getRunId());
    }

    /* (non-Javadoc)
     * @see com.xtr.comm.task.TaskListener#onTaskStart(com.xtr.comm.task.TaskContext)
     */
    public void onTaskStart(TaskContext taskContext) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("onTaskStart--" + taskContext.getTask().getRunId());

    }

    /* (non-Javadoc)
     * @see com.xtr.comm.task.TaskListener#onTaskSuccess(com.xtr.comm.task.TaskContext)
     */
    public void onTaskSuccess(TaskContext taskContext) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("onTaskSuccess--" + taskContext.getTask().getRunId());

    }

    /* (non-Javadoc)
     * @see com.xtr.comm.task.TaskListener#onTaskFail(com.xtr.comm.task.TaskContext)
     */
    public void onTaskFail(TaskContext taskContext) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("onTaskFail--" + taskContext.getTask().getRunId());
    }

}
