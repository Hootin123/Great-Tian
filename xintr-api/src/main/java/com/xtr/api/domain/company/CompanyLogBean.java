package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class CompanyLogBean extends BaseObject implements Serializable {


    private String dateStr;//字符串时间
    private String typeStr;//0:成功 其他都是失败

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    /**
     * 主键,所属表字段为company_log.id
     */
    private Long id;

    /**
     * 操作人,所属表字段为company_log.user_id
     */
    private Long userId;

    /**
     * 创建时间,所属表字段为company_log.create_time
     */
    private Date createTime;

    /**
     * 日志内容,所属表字段为company_log.content
     */
    private String content;

    /**
     * 用户所做的操作,所属表字段为company_log.operation
     */
    private String operation;

    /**
     * 所属模块名称,所属表字段为company_log.model_name
     */
    private String modelName;

    /**
     * 日志类型 0：操作日志 1：异常日志,所属表字段为company_log.type
     */
    private Integer type;

    /**
     * 请求ip,所属表字段为company_log.request_ip
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

    private Long memberCompanyId;

    /**
     * 获取 主键 字段:company_log.id
     *
     * @return company_log.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:company_log.id
     *
     * @param id company_log.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 操作人 字段:company_log.user_id
     *
     * @return company_log.user_id, 操作人
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置 操作人 字段:company_log.user_id
     *
     * @param userId company_log.user_id, 操作人
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取 创建时间 字段:company_log.create_time
     *
     * @return company_log.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:company_log.create_time
     *
     * @param createTime company_log.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 日志内容 字段:company_log.content
     *
     * @return company_log.content, 日志内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置 日志内容 字段:company_log.content
     *
     * @param content company_log.content, 日志内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取 用户所做的操作 字段:company_log.operation
     *
     * @return company_log.operation, 用户所做的操作
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置 用户所做的操作 字段:company_log.operation
     *
     * @param operation company_log.operation, 用户所做的操作
     */
    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    /**
     * 获取 所属模块名称 字段:company_log.model_name
     *
     * @return company_log.model_name, 所属模块名称
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 设置 所属模块名称 字段:company_log.model_name
     *
     * @param modelName company_log.model_name, 所属模块名称
     */
    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    /**
     * 获取 日志类型 0：操作日志 1：异常日志 字段:company_log.type
     *
     * @return company_log.type, 日志类型 0：操作日志 1：异常日志
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 日志类型 0：操作日志 1：异常日志 字段:company_log.type
     *
     * @param type company_log.type, 日志类型 0：操作日志 1：异常日志
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取 请求ip 字段:company_log.request_ip
     *
     * @return company_log.request_ip, 请求ip
     */
    public String getRequestIp() {
        return requestIp;
    }

    /**
     * 设置 请求ip 字段:company_log.request_ip
     *
     * @param requestIp company_log.request_ip, 请求ip
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

    public Long getMemberCompanyId() {
        return memberCompanyId;
    }

    public void setMemberCompanyId(Long memberCompanyId) {
        this.memberCompanyId = memberCompanyId;
    }
}