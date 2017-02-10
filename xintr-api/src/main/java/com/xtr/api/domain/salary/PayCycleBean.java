package com.xtr.api.domain.salary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayCycleBean implements Serializable {
    /**
     * 主键,所属表字段为pay_cycle.id
     */
    private Long id;

    /**
     * 工资主键,所属表字段为pay_cycle.company_id
     */
    private Long companyId;

    /**
     * 是否生成工资单 0:未生成 1:已生成,所属表字段为pay_cycle.is_generate_payroll
     */
    private Integer isGeneratePayroll;

    /**
     * 当前计薪周期,所属表字段为pay_cycle.current_pay_cycle
     */
    private String currentPayCycle;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 年度
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 当月工资是否已发放 0:未发放 1:待发放  2:发放中  3:已发放
     */
    private Integer isPayOff;

    /**
     * 工资单审批状态: 0:未审批  1:审批通过
     */
    private Integer approvalState;

    /**
     * 人工成功
     */
    private BigDecimal laborCost;

    /**
     * 发放总额
     */
    private BigDecimal totalWages;

    /**
     * 发薪日期
     */
    private Date payDay;

    /**
     * 发薪人数
     */
    private Integer peopleNumber;

    /**
     * 税前工资
     */
    private BigDecimal pretaxPayroll;

    /**
     * 社保公积金
     */
    private BigDecimal socialSecurityFund;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 获取 主键 字段:pay_cycle.id
     *
     * @return pay_cycle.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:pay_cycle.id
     *
     * @param id pay_cycle.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 工资主键 字段:pay_cycle.company_id
     *
     * @return pay_cycle.company_id, 工资主键
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 工资主键 字段:pay_cycle.company_id
     *
     * @param companyId pay_cycle.company_id, 工资主键
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 是否生成工资单 0:未生成 1:已生成 字段:pay_cycle.is_generate_payroll
     *
     * @return pay_cycle.is_generate_payroll, 是否生成工资单 0:未生成 1:已生成
     */
    public Integer getIsGeneratePayroll() {
        return isGeneratePayroll;
    }

    /**
     * 设置 是否生成工资单 0:未生成 1:已生成 字段:pay_cycle.is_generate_payroll
     *
     * @param isGeneratePayroll pay_cycle.is_generate_payroll, 是否生成工资单 0:未生成 1:已生成
     */
    public void setIsGeneratePayroll(Integer isGeneratePayroll) {
        this.isGeneratePayroll = isGeneratePayroll;
    }

    /**
     * 获取 当前计薪周期 字段:pay_cycle.current_pay_cycle
     *
     * @return pay_cycle.current_pay_cycle, 当前计薪周期
     */
    public String getCurrentPayCycle() {
        return currentPayCycle;
    }

    /**
     * 设置 当前计薪周期 字段:pay_cycle.current_pay_cycle
     *
     * @param currentPayCycle pay_cycle.current_pay_cycle, 当前计薪周期
     */
    public void setCurrentPayCycle(String currentPayCycle) {
        this.currentPayCycle = currentPayCycle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getIsPayOff() {
        return isPayOff;
    }

    public void setIsPayOff(Integer isPayOff) {
        this.isPayOff = isPayOff;
    }

    public Integer getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(Integer approvalState) {
        this.approvalState = approvalState;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTotalWages() {
        return totalWages;
    }

    public void setTotalWages(BigDecimal totalWages) {
        this.totalWages = totalWages;
    }

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public BigDecimal getPretaxPayroll() {
        return pretaxPayroll;
    }

    public void setPretaxPayroll(BigDecimal pretaxPayroll) {
        this.pretaxPayroll = pretaxPayroll;
    }

    public BigDecimal getSocialSecurityFund() {
        return socialSecurityFund;
    }

    public void setSocialSecurityFund(BigDecimal socialSecurityFund) {
        this.socialSecurityFund = socialSecurityFund;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}