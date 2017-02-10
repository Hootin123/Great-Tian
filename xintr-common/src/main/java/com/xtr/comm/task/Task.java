package com.xtr.comm.task;

import java.util.Date;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 13:20
 */
public interface Task {

    /**
     * 未执行
     */
    int NOMAL = 0;

    /**
     * 运行中
     */
    int RUNNING = 1;

    /**
     * 成功
     */
    int SUCESS = 2;

    /**
     * 失败
     */
    int FAIL = 3;

    /**
     * 任务名
     *
     * @return
     */
    String getName();

    /**
     * 执行序号ID
     *
     * @return
     */
    String getRunId();

    /**
     * 停止task
     */
    void stop();

    /**
     * 是否正在运行中
     *
     * @return
     */
    boolean isRunning();

    /**
     * 是否执行成功
     *
     * @return
     */
    boolean isSuccess();

    /**
     * 是否失败
     *
     * @return
     */
    boolean isFailed();

    /**
     * 获取状态
     *
     * @return
     */
    int getStatus();

    /**
     * 设置状态
     *
     * @param status
     */
    void setStatus(int status);

    /**
     * 任务启动时间
     *
     * @return
     */
    Date getStartTime();

    /**
     * 任务执行时间
     *
     * @return
     */
    Date getEndTime();

    /**
     * 任务失败时间
     *
     * @return
     */
    String getFailCause();


}
