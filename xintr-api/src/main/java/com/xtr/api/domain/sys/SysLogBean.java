package com.xtr.api.domain.sys;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class SysLogBean extends BaseObject implements Serializable {
    /**
     * 主键,所属表字段为sys_log.id
     */
    private Long id;

    /**
     * 操作人,所属表字段为sys_log.user_id
     */
    private Long userId;

    /**
     * 创建时间,所属表字段为sys_log.create_time
     */
    private Date createTime;

    /**
     * 日志内容,所属表字段为sys_log.content
     */
    private String content;

    /**
     * 用户所做的操作,所属表字段为sys_log.operation
     */
    private String operation;

    /**
     * 所属模块名称,所属表字段为sys_log.model_name
     */
    private String modelName;

    /**
     * 日志类型 0：操作日志 1：异常日志,所属表字段为sys_log.type
     */
    private Integer type;

    /**
     * 请求ip,所属表字段为sys_log.request_ip
     */
    private String requestIp;

    /**
     * 服务器名
     */
    private String serverName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 获取 主键 字段:sys_log.id
     *
     * @return sys_log.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:sys_log.id
     *
     * @param id sys_log.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 操作人 字段:sys_log.user_id
     *
     * @return sys_log.user_id, 操作人
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置 操作人 字段:sys_log.user_id
     *
     * @param userId sys_log.user_id, 操作人
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取 创建时间 字段:sys_log.create_time
     *
     * @return sys_log.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:sys_log.create_time
     *
     * @param createTime sys_log.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 日志内容 字段:sys_log.content
     *
     * @return sys_log.content, 日志内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置 日志内容 字段:sys_log.content
     *
     * @param content sys_log.content, 日志内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取 用户所做的操作 字段:sys_log.operation
     *
     * @return sys_log.operation, 用户所做的操作
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置 用户所做的操作 字段:sys_log.operation
     *
     * @param operation sys_log.operation, 用户所做的操作
     */
    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    /**
     * 获取 所属模块名称 字段:sys_log.model_name
     *
     * @return sys_log.model_name, 所属模块名称
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 设置 所属模块名称 字段:sys_log.model_name
     *
     * @param modelName sys_log.model_name, 所属模块名称
     */
    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    /**
     * 获取 日志类型 0：操作日志 1：异常日志 字段:sys_log.type
     *
     * @return sys_log.type, 日志类型 0：操作日志 1：异常日志
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 日志类型 0：操作日志 1：异常日志 字段:sys_log.type
     *
     * @param type sys_log.type, 日志类型 0：操作日志 1：异常日志
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取 请求ip 字段:sys_log.request_ip
     *
     * @return sys_log.request_ip, 请求ip
     */
    public String getRequestIp() {
        return requestIp;
    }

    /**
     * 设置 请求ip 字段:sys_log.request_ip
     *
     * @param requestIp sys_log.request_ip, 请求ip
     */
    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp == null ? null : requestIp.trim();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}