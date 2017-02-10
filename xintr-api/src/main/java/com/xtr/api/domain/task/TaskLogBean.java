package com.xtr.api.domain.task;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class TaskLogBean extends BaseObject implements Serializable{
    /**
     *  主键,所属表字段为task_log.id
     */
    private Long id;

    /**
     *  任务名称,所属表字段为task_log.task_name
     */
    private String taskName;

    /**
     *  任务id,所属表字段为task_log.task_id
     */
    private String taskId;

    /**
     *  任务开始时间,所属表字段为task_log.start_time
     */
    private Date startTime;

    /**
     *  任务结束时间,所属表字段为task_log.end_time
     */
    private Date endTime;

    /**
     *  操作员id,所属表字段为task_log.user_id
     */
    private Long userId;

    /**
     *  创建时间,所属表字段为task_log.create_time
     */
    private Date createTime;

    /**
     *  日志类型 0:后台任务,所属表字段为task_log.type
     */
    private Integer type;

    /**
     *  耗时,所属表字段为task_log.took_time
     */
    private String tookTime;

    /**
     *  服务器名,所属表字段为task_log.server_name
     */
    private String serverName;

    /**
     *  服务器ip,所属表字段为task_log.server_ip
     */
    private String serverIp;

    /**
     *  说明,所属表字段为task_log.remarks
     */
    private String remarks;

    /**
     *  任务状态 0:成功 1:失败,所属表字段为task_log.state
     */
    private Integer state;

    /**
     * 获取 主键 字段:task_log.id
     *
     * @return task_log.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:task_log.id
     *
     * @param id task_log.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 任务名称 字段:task_log.task_name
     *
     * @return task_log.task_name, 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置 任务名称 字段:task_log.task_name
     *
     * @param taskName task_log.task_name, 任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 获取 任务id 字段:task_log.task_id
     *
     * @return task_log.task_id, 任务id
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 设置 任务id 字段:task_log.task_id
     *
     * @param taskId task_log.task_id, 任务id
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * 获取 任务开始时间 字段:task_log.start_time
     *
     * @return task_log.start_time, 任务开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置 任务开始时间 字段:task_log.start_time
     *
     * @param startTime task_log.start_time, 任务开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取 任务结束时间 字段:task_log.end_time
     *
     * @return task_log.end_time, 任务结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置 任务结束时间 字段:task_log.end_time
     *
     * @param endTime task_log.end_time, 任务结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取 操作员id 字段:task_log.user_id
     *
     * @return task_log.user_id, 操作员id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置 操作员id 字段:task_log.user_id
     *
     * @param userId task_log.user_id, 操作员id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取 创建时间 字段:task_log.create_time
     *
     * @return task_log.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:task_log.create_time
     *
     * @param createTime task_log.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 日志类型 0:后台任务 字段:task_log.type
     *
     * @return task_log.type, 日志类型 0:后台任务
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 日志类型 0:后台任务 字段:task_log.type
     *
     * @param type task_log.type, 日志类型 0:后台任务
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取 耗时 字段:task_log.took_time
     *
     * @return task_log.took_time, 耗时
     */
    public String getTookTime() {
        return tookTime;
    }

    /**
     * 设置 耗时 字段:task_log.took_time
     *
     * @param tookTime task_log.took_time, 耗时
     */
    public void setTookTime(String tookTime) {
        this.tookTime = tookTime == null ? null : tookTime.trim();
    }

    /**
     * 获取 服务器名 字段:task_log.server_name
     *
     * @return task_log.server_name, 服务器名
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * 设置 服务器名 字段:task_log.server_name
     *
     * @param serverName task_log.server_name, 服务器名
     */
    public void setServerName(String serverName) {
        this.serverName = serverName == null ? null : serverName.trim();
    }

    /**
     * 获取 服务器ip 字段:task_log.server_ip
     *
     * @return task_log.server_ip, 服务器ip
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * 设置 服务器ip 字段:task_log.server_ip
     *
     * @param serverIp task_log.server_ip, 服务器ip
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
    }

    /**
     * 获取 说明 字段:task_log.remarks
     *
     * @return task_log.remarks, 说明
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置 说明 字段:task_log.remarks
     *
     * @param remarks task_log.remarks, 说明
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    /**
     * 获取 任务状态 0:成功 1:失败 字段:task_log.state
     *
     * @return task_log.state, 任务状态 0:成功 1:失败
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置 任务状态 0:成功 1:失败 字段:task_log.state
     *
     * @param state task_log.state, 任务状态 0:成功 1:失败
     */
    public void setState(Integer state) {
        this.state = state;
    }
}