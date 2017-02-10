package com.xtr.api.domain.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class PayRuleBean extends BaseObject implements Serializable {
    /**
     *  主键,所属表字段为pay_rule.id
     */
    private Long id;

    /**
     *  公司id,所属表字段为pay_rule.company_id
     */
    private Long companyId;

    /**
     *  部门Id,所属表字段为pay_rule.dept_id
     */
    private Long deptId;

    /**
     *  企业用户Id,所属表字段为pay_rule.company_menber_id
     */
    private Long companyMenberId;

    /**
     *  计薪开始日期方式 0：当月 1：次月,所属表字段为pay_rule.pay_start_type
     */
    private Integer payStartType;

    /**
     *  计薪开始日,所属表字段为pay_rule.pay_start_day
     */
    private String payStartDay;

    /**
     *  计薪结束日期方式 0：当月 1：次月,所属表字段为pay_rule.pay_end_type
     */
    private Integer payEndType;

    /**
     *  计薪结束日,所属表字段为pay_rule.pay_end_day
     */
    private String payEndDay;

    /**
     *  发薪日期方式 0：当月 1：次月,所属表字段为pay_rule.pay_day_type
     */
    private Integer payDayType;

    /**
     *  发薪日,所属表字段为pay_rule.pay_day
     */
    private String payDay;

    /**
     *  计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法,所属表字段为pay_rule.pay_way
     */
    private Integer payWay;

    /**
     *  计薪临界点  0:以11天为临界点  2:以21.75天为临界点,所属表字段为pay_rule.pay_critical_point
     */
    private Integer payCriticalPoint;

    /**
     *  是否计算社保公积金 0:否  1:是,所属表字段为pay_rule.is_social_security
     */
    private String isSocialSecurity;

    /**
     *  创建时间,所属表字段为pay_rule.create_time
     */
    private Date createTime;

    /**
     *  获取 主键 字段:pay_rule.id 
     *
     *  @return pay_rule.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     *  设置 主键 字段:pay_rule.id 
     *
     *  @param id pay_rule.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *  获取 公司id 字段:pay_rule.company_id 
     *
     *  @return pay_rule.company_id, 公司id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     *  设置 公司id 字段:pay_rule.company_id 
     *
     *  @param companyId pay_rule.company_id, 公司id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     *  获取 部门Id 字段:pay_rule.dept_id 
     *
     *  @return pay_rule.dept_id, 部门Id
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     *  设置 部门Id 字段:pay_rule.dept_id 
     *
     *  @param deptId pay_rule.dept_id, 部门Id
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     *  获取 企业用户Id 字段:pay_rule.company_menber_id 
     *
     *  @return pay_rule.company_menber_id, 企业用户Id
     */
    public Long getCompanyMenberId() {
        return companyMenberId;
    }

    /**
     *  设置 企业用户Id 字段:pay_rule.company_menber_id 
     *
     *  @param companyMenberId pay_rule.company_menber_id, 企业用户Id
     */
    public void setCompanyMenberId(Long companyMenberId) {
        this.companyMenberId = companyMenberId;
    }

    /**
     *  获取 计薪开始日期方式 0：当月 1：次月 字段:pay_rule.pay_start_type 
     *
     *  @return pay_rule.pay_start_type, 计薪开始日期方式 0：当月 1：次月
     */
    public Integer getPayStartType() {
        return payStartType;
    }

    /**
     *  设置 计薪开始日期方式 0：当月 1：次月 字段:pay_rule.pay_start_type 
     *
     *  @param payStartType pay_rule.pay_start_type, 计薪开始日期方式 0：当月 1：次月
     */
    public void setPayStartType(Integer payStartType) {
        this.payStartType = payStartType;
    }

    /**
     *  获取 计薪开始日 字段:pay_rule.pay_start_day 
     *
     *  @return pay_rule.pay_start_day, 计薪开始日
     */
    public String getPayStartDay() {
        return payStartDay;
    }

    /**
     *  设置 计薪开始日 字段:pay_rule.pay_start_day 
     *
     *  @param payStartDay pay_rule.pay_start_day, 计薪开始日
     */
    public void setPayStartDay(String payStartDay) {
        this.payStartDay = payStartDay == null ? null : payStartDay.trim();
    }

    /**
     *  获取 计薪结束日期方式 0：当月 1：次月 字段:pay_rule.pay_end_type 
     *
     *  @return pay_rule.pay_end_type, 计薪结束日期方式 0：当月 1：次月
     */
    public Integer getPayEndType() {
        return payEndType;
    }

    /**
     *  设置 计薪结束日期方式 0：当月 1：次月 字段:pay_rule.pay_end_type 
     *
     *  @param payEndType pay_rule.pay_end_type, 计薪结束日期方式 0：当月 1：次月
     */
    public void setPayEndType(Integer payEndType) {
        this.payEndType = payEndType;
    }

    /**
     *  获取 计薪结束日 字段:pay_rule.pay_end_day 
     *
     *  @return pay_rule.pay_end_day, 计薪结束日
     */
    public String getPayEndDay() {
        return payEndDay;
    }

    /**
     *  设置 计薪结束日 字段:pay_rule.pay_end_day 
     *
     *  @param payEndDay pay_rule.pay_end_day, 计薪结束日
     */
    public void setPayEndDay(String payEndDay) {
        this.payEndDay = payEndDay == null ? null : payEndDay.trim();
    }

    /**
     *  获取 发薪日期方式 0：当月 1：次月 字段:pay_rule.pay_day_type 
     *
     *  @return pay_rule.pay_day_type, 发薪日期方式 0：当月 1：次月
     */
    public Integer getPayDayType() {
        return payDayType;
    }

    /**
     *  设置 发薪日期方式 0：当月 1：次月 字段:pay_rule.pay_day_type 
     *
     *  @param payDayType pay_rule.pay_day_type, 发薪日期方式 0：当月 1：次月
     */
    public void setPayDayType(Integer payDayType) {
        this.payDayType = payDayType;
    }

    /**
     *  获取 发薪日 字段:pay_rule.pay_day 
     *
     *  @return pay_rule.pay_day, 发薪日
     */
    public String getPayDay() {
        return payDay;
    }

    /**
     *  设置 发薪日 字段:pay_rule.pay_day 
     *
     *  @param payDay pay_rule.pay_day, 发薪日
     */
    public void setPayDay(String payDay) {
        this.payDay = payDay == null ? null : payDay.trim();
    }

    /**
     *  获取 计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法 字段:pay_rule.pay_way 
     *
     *  @return pay_rule.pay_way, 计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法
     */
    public Integer getPayWay() {
        return payWay;
    }

    /**
     *  设置 计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法 字段:pay_rule.pay_way 
     *
     *  @param payWay pay_rule.pay_way, 计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法
     */
    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    /**
     *  获取 计薪临界点  0:以11天为临界点  2:以21.75天为临界点 字段:pay_rule.pay_critical_point 
     *
     *  @return pay_rule.pay_critical_point, 计薪临界点  0:以11天为临界点  2:以21.75天为临界点
     */
    public Integer getPayCriticalPoint() {
        return payCriticalPoint;
    }

    /**
     *  设置 计薪临界点  0:以11天为临界点  2:以21.75天为临界点 字段:pay_rule.pay_critical_point 
     *
     *  @param payCriticalPoint pay_rule.pay_critical_point, 计薪临界点  0:以11天为临界点  2:以21.75天为临界点
     */
    public void setPayCriticalPoint(Integer payCriticalPoint) {
        this.payCriticalPoint = payCriticalPoint;
    }

    /**
     *  获取 是否计算社保公积金 0:否  1:是 字段:pay_rule.is_social_security 
     *
     *  @return pay_rule.is_social_security, 是否计算社保公积金 0:否  1:是
     */
    public String getIsSocialSecurity() {
        return isSocialSecurity;
    }

    /**
     *  设置 是否计算社保公积金 0:否  1:是 字段:pay_rule.is_social_security 
     *
     *  @param isSocialSecurity pay_rule.is_social_security, 是否计算社保公积金 0:否  1:是
     */
    public void setIsSocialSecurity(String isSocialSecurity) {
        this.isSocialSecurity = isSocialSecurity == null ? null : isSocialSecurity.trim();
    }

    /**
     *  获取 创建时间 字段:pay_rule.create_time 
     *
     *  @return pay_rule.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     *  设置 创建时间 字段:pay_rule.create_time 
     *
     *  @param createTime pay_rule.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}