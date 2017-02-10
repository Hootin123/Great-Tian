package com.xtr.api.domain.salary;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerSalaryRecordBean {
    /**
     *  ,所属表字段为customer_salary_record.salary_record_id
     */
    private Long salaryRecordId;

    /**
     *  员工ID,所属表字段为customer_salary_record.customer_id
     */
    private Long customerId;

    /**
     *  上次工资基数,所属表字段为customer_salary_record.old_salary
     */
    private BigDecimal oldSalary;

    /**
     *  调整后的工资基数,所属表字段为customer_salary_record.new_salary
     */
    private BigDecimal newSalary;

    /**
     *  生效日期,所属表字段为customer_salary_record.effective_date
     */
    private Date effectiveDate;

    /**
     *  原因,所属表字段为customer_salary_record.reason
     */
    private String reason;

    /**
     *  创建时间,所属表字段为customer_salary_record.create_time
     */
    private Date createTime;

    /**
     *  调薪操作人,所属表字段为customer_salary_record.create_user
     */
    private Long createUser;

    /**
     *  状态，1 新增；2已生效,所属表字段为customer_salary_record.state
     */
    private Integer state;

    /**
     * 获取  字段:customer_salary_record.salary_record_id
     *
     * @return customer_salary_record.salary_record_id, 
     */
    public Long getSalaryRecordId() {
        return salaryRecordId;
    }

    /**
     * 设置  字段:customer_salary_record.salary_record_id
     *
     * @param salaryRecordId customer_salary_record.salary_record_id, 
     */
    public void setSalaryRecordId(Long salaryRecordId) {
        this.salaryRecordId = salaryRecordId;
    }

    /**
     * 获取 员工ID 字段:customer_salary_record.customer_id
     *
     * @return customer_salary_record.customer_id, 员工ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置 员工ID 字段:customer_salary_record.customer_id
     *
     * @param customerId customer_salary_record.customer_id, 员工ID
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取 上次工资基数 字段:customer_salary_record.old_salary
     *
     * @return customer_salary_record.old_salary, 上次工资基数
     */
    public BigDecimal getOldSalary() {
        return oldSalary;
    }

    /**
     * 设置 上次工资基数 字段:customer_salary_record.old_salary
     *
     * @param oldSalary customer_salary_record.old_salary, 上次工资基数
     */
    public void setOldSalary(BigDecimal oldSalary) {
        this.oldSalary = oldSalary;
    }

    /**
     * 获取 调整后的工资基数 字段:customer_salary_record.new_salary
     *
     * @return customer_salary_record.new_salary, 调整后的工资基数
     */
    public BigDecimal getNewSalary() {
        return newSalary;
    }

    /**
     * 设置 调整后的工资基数 字段:customer_salary_record.new_salary
     *
     * @param newSalary customer_salary_record.new_salary, 调整后的工资基数
     */
    public void setNewSalary(BigDecimal newSalary) {
        this.newSalary = newSalary;
    }

    /**
     * 获取 生效日期 字段:customer_salary_record.effective_date
     *
     * @return customer_salary_record.effective_date, 生效日期
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * 设置 生效日期 字段:customer_salary_record.effective_date
     *
     * @param effectiveDate customer_salary_record.effective_date, 生效日期
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * 获取 原因 字段:customer_salary_record.reason
     *
     * @return customer_salary_record.reason, 原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置 原因 字段:customer_salary_record.reason
     *
     * @param reason customer_salary_record.reason, 原因
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * 获取 创建时间 字段:customer_salary_record.create_time
     *
     * @return customer_salary_record.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:customer_salary_record.create_time
     *
     * @param createTime customer_salary_record.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 调薪操作人 字段:customer_salary_record.create_user
     *
     * @return customer_salary_record.create_user, 调薪操作人
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 设置 调薪操作人 字段:customer_salary_record.create_user
     *
     * @param createUser customer_salary_record.create_user, 调薪操作人
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取 状态，1 新增；2已生效 字段:customer_salary_record.state
     *
     * @return customer_salary_record.state, 状态，1 新增；2已生效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置 状态，1 新增；2已生效 字段:customer_salary_record.state
     *
     * @param state customer_salary_record.state, 状态，1 新增；2已生效
     */
    public void setState(Integer state) {
        this.state = state;
    }
}