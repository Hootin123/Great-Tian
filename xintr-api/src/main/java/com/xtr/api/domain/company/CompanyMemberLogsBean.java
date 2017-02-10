package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class CompanyMemberLogsBean extends BaseObject implements Serializable{
    /**
     *  id,所属表字段为company_member_logs.log_id
     */
    private Long logId;

    /**
     *  企业id,所属表字段为company_member_logs.log_company_id
     */
    private Long logCompanyId;

    /**
     *  子公司id,所属表字段为company_member_logs.log_dep_id
     */
    private Long logDepId;

    /**
     *  成员id,所属表字段为company_member_logs.log_member_id
     */
    private Long logMemberId;

    /**
     *  日志描述,所属表字段为company_member_logs.log_des
     */
    private String logDes;

    /**
     *  日志类型 1......,所属表字段为company_member_logs.log_type
     */
    private Integer logType;

    /**
     *  创建时间,所属表字段为company_member_logs.log_addtime
     */
    private Date logAddtime;

    /**
     *  登录时间,所属表字段为company_member_logs.log_member_logintime
     */
    private Date logMemberLogintime;

    /**
     *  退出时间,所属表字段为company_member_logs.log_member_logouttime
     */
    private Date logMemberLogouttime;

    /**
     *  用户访问IP,所属表字段为company_member_logs.log_ip
     */
    private String logIp;

    /**
     *  获取 id 字段:company_member_logs.log_id 
     *
     *  @return company_member_logs.log_id, id
     */
    public Long getLogId() {
        return logId;
    }

    /**
     *  设置 id 字段:company_member_logs.log_id 
     *
     *  @param logId company_member_logs.log_id, id
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    /**
     *  获取 企业id 字段:company_member_logs.log_company_id 
     *
     *  @return company_member_logs.log_company_id, 企业id
     */
    public Long getLogCompanyId() {
        return logCompanyId;
    }

    /**
     *  设置 企业id 字段:company_member_logs.log_company_id 
     *
     *  @param logCompanyId company_member_logs.log_company_id, 企业id
     */
    public void setLogCompanyId(Long logCompanyId) {
        this.logCompanyId = logCompanyId;
    }

    /**
     *  获取 子公司id 字段:company_member_logs.log_dep_id 
     *
     *  @return company_member_logs.log_dep_id, 子公司id
     */
    public Long getLogDepId() {
        return logDepId;
    }

    /**
     *  设置 子公司id 字段:company_member_logs.log_dep_id 
     *
     *  @param logDepId company_member_logs.log_dep_id, 子公司id
     */
    public void setLogDepId(Long logDepId) {
        this.logDepId = logDepId;
    }

    /**
     *  获取 成员id 字段:company_member_logs.log_member_id 
     *
     *  @return company_member_logs.log_member_id, 成员id
     */
    public Long getLogMemberId() {
        return logMemberId;
    }

    /**
     *  设置 成员id 字段:company_member_logs.log_member_id 
     *
     *  @param logMemberId company_member_logs.log_member_id, 成员id
     */
    public void setLogMemberId(Long logMemberId) {
        this.logMemberId = logMemberId;
    }

    /**
     *  获取 日志描述 字段:company_member_logs.log_des 
     *
     *  @return company_member_logs.log_des, 日志描述
     */
    public String getLogDes() {
        return logDes;
    }

    /**
     *  设置 日志描述 字段:company_member_logs.log_des 
     *
     *  @param logDes company_member_logs.log_des, 日志描述
     */
    public void setLogDes(String logDes) {
        this.logDes = logDes == null ? null : logDes.trim();
    }

    /**
     *  获取 日志类型 1...... 字段:company_member_logs.log_type 
     *
     *  @return company_member_logs.log_type, 日志类型 1......
     */
    public Integer getLogType() {
        return logType;
    }

    /**
     *  设置 日志类型 1...... 字段:company_member_logs.log_type 
     *
     *  @param logType company_member_logs.log_type, 日志类型 1......
     */
    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    /**
     *  获取 创建时间 字段:company_member_logs.log_addtime 
     *
     *  @return company_member_logs.log_addtime, 创建时间
     */
    public Date getLogAddtime() {
        return logAddtime;
    }

    /**
     *  设置 创建时间 字段:company_member_logs.log_addtime 
     *
     *  @param logAddtime company_member_logs.log_addtime, 创建时间
     */
    public void setLogAddtime(Date logAddtime) {
        this.logAddtime = logAddtime;
    }

    /**
     *  获取 登录时间 字段:company_member_logs.log_member_logintime 
     *
     *  @return company_member_logs.log_member_logintime, 登录时间
     */
    public Date getLogMemberLogintime() {
        return logMemberLogintime;
    }

    /**
     *  设置 登录时间 字段:company_member_logs.log_member_logintime 
     *
     *  @param logMemberLogintime company_member_logs.log_member_logintime, 登录时间
     */
    public void setLogMemberLogintime(Date logMemberLogintime) {
        this.logMemberLogintime = logMemberLogintime;
    }

    /**
     *  获取 退出时间 字段:company_member_logs.log_member_logouttime 
     *
     *  @return company_member_logs.log_member_logouttime, 退出时间
     */
    public Date getLogMemberLogouttime() {
        return logMemberLogouttime;
    }

    /**
     *  设置 退出时间 字段:company_member_logs.log_member_logouttime 
     *
     *  @param logMemberLogouttime company_member_logs.log_member_logouttime, 退出时间
     */
    public void setLogMemberLogouttime(Date logMemberLogouttime) {
        this.logMemberLogouttime = logMemberLogouttime;
    }

    /**
     *  获取 用户访问IP 字段:company_member_logs.log_ip 
     *
     *  @return company_member_logs.log_ip, 用户访问IP
     */
    public String getLogIp() {
        return logIp;
    }

    /**
     *  设置 用户访问IP 字段:company_member_logs.log_ip 
     *
     *  @param logIp company_member_logs.log_ip, 用户访问IP
     */
    public void setLogIp(String logIp) {
        this.logIp = logIp == null ? null : logIp.trim();
    }
}