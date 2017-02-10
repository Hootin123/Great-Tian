package com.xtr.comm.task;

import java.util.Date;

/**
 * <p>定时任务抽象类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 13:02
 */
public abstract class AbstractTask implements Task {


    /**
     * 任务名
     */
    private String name;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务状态
     */
    private int status;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 超时时间：秒
     */
    public int timeOut = 3600;

    /**
     * 失败原因
     */
    private String failCause;

    /**
     * 执行数量
     */
    private int recordNumber;

    public AbstractTask() {
    }

    public AbstractTask(String taskName) {
        this.name = taskName;
    }

    /**
     * 停止任务
     */
    public void stop() {
        throw new IllegalArgumentException("stop()  method to implemented!");
    }

    /**
     * 检测并处理超时的task
     */
    public abstract void checkTimeOut();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
}
