package com.xtr.api.domain.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomerPayrollBean extends BaseObject implements Serializable {
    /**
     * 主键,所属表字段为customer_payroll.id
     */
    private Long id;

    /**
     * 员工id,所属表字段为customer_payroll.customer_id
     */
    private Long customerId;

    /**
     * 部门id,所属表字段为customer_payroll.dept_id
     */
    private Long deptId;

    /**
     * 公司id,所属表字段为customer_payroll.company_id
     */
    private Long companyId;

    /**
     * 计薪周期Id
     */
    private Long payCycleId;

    /**
     * 基本工资,所属表字段为customer_payroll.base_wages
     */
    private BigDecimal baseWages;

    /**
     * 调薪后工资,所属表字段为customer_payroll.add_wages
     */
    private BigDecimal addWages;

    /**
     * 考勤扣款,所属表字段为customer_payroll.attendance_deduction
     */
    private BigDecimal attendanceDeduction;

    /**
     * 缺勤天数,所属表字段为customer_payroll.absence_day_number
     */
    private BigDecimal absenceDayNumber;

    /**
     * 津贴总额,所属表字段为customer_payroll.total_allowance
     */
    private BigDecimal totalAllowance;

    /**
     * 奖金总额
     */
    private BigDecimal totalBonus;

    /**
     * 社保公积金,所属表字段为customer_payroll.social_security_fund
     */
    private BigDecimal socialSecurityFund;

    /**
     * 社保,所属表字段为customer_payroll.social_security
     */
    private BigDecimal socialSecurity;

    /**
     * 公积金,所属表字段为customer_payroll.fund
     */
    private BigDecimal fund;

    /**
     * 税前补发/扣款,所属表字段为customer_payroll.pretax
     */
    private BigDecimal pretax;

    /**
     * 应发工资,所属表字段为customer_payroll.should_amount
     */
    private BigDecimal shouldAmount;

    /**
     * 个税,所属表字段为customer_payroll.personal_tax
     */
    private BigDecimal personalTax;

    /**
     * 税后补发/扣款,所属表字段为customer_payroll.after_tax
     */
    private BigDecimal afterTax;

    /**
     * 实发工资,所属表字段为customer_payroll.real_wage
     */
    private BigDecimal realWage;

    /**
     * 创建时间,所属表字段为customer_payroll.create_time
     */
    private Date createTime;

    /**
     * 津贴设置是否被更新 0:未更新  1:已更新,所属表字段为customer_payroll.is_allowance_update
     */
    private Integer isAllowanceUpdate;

    /**
     * 奖金设置是否被更新 0:未更新  1:已更新,所属表字段为customer_payroll.is_bonus_update
     */
    private Integer isBonusUpdate;

    /**
     * 计薪规则是否被更新 0:未更新  1:已更新,所属表字段为customer_payroll.is_payrule_update
     */
    private Integer isPayruleUpdate;

    /**
     * 工资单是否已发 0:未发 1:已发
     */
    private Integer isPayOff;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 财务操作人
     */
    private String financeUser;

    /**
     * 支付失败信息
     */
    private String failMsg;

    /**
     * 报销金额
     */
    private BigDecimal wipedAmount;

    /**
     * 绩效补充
     */
    private BigDecimal appraisalsSupplement;

    /**
     * 奖金明细
     */
    private String bonusJsonStr;

    /**
     * 津贴明细
     */
    private String allowanceJsonStr;

    /**
     * 社保公积金(公司部分)
     */
    private BigDecimal socialSecurityFundCorp;

    /**
     * 获取 主键 字段:customer_payroll.id
     *
     * @return customer_payroll.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:customer_payroll.id
     *
     * @param id customer_payroll.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 员工id 字段:customer_payroll.customer_id
     *
     * @return customer_payroll.customer_id, 员工id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置 员工id 字段:customer_payroll.customer_id
     *
     * @param customerId customer_payroll.customer_id, 员工id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取 部门id 字段:customer_payroll.dept_id
     *
     * @return customer_payroll.dept_id, 部门id
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置 部门id 字段:customer_payroll.dept_id
     *
     * @param deptId customer_payroll.dept_id, 部门id
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取 公司id 字段:customer_payroll.company_id
     *
     * @return customer_payroll.company_id, 公司id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 公司id 字段:customer_payroll.company_id
     *
     * @param companyId customer_payroll.company_id, 公司id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 基本工资 字段:customer_payroll.base_wages
     *
     * @return customer_payroll.base_wages, 基本工资
     */
    public BigDecimal getBaseWages() {
        return baseWages;
    }

    /**
     * 设置 基本工资 字段:customer_payroll.base_wages
     *
     * @param baseWages customer_payroll.base_wages, 基本工资
     */
    public void setBaseWages(BigDecimal baseWages) {
        this.baseWages = baseWages;
    }

    /**
     * 获取 调薪后工资 字段:customer_payroll.add_wages
     *
     * @return customer_payroll.add_wages, 调薪后工资
     */
    public BigDecimal getAddWages() {
        return addWages;
    }

    /**
     * 设置 调薪后工资 字段:customer_payroll.add_wages
     *
     * @param addWages customer_payroll.add_wages, 调薪后工资
     */
    public void setAddWages(BigDecimal addWages) {
        this.addWages = addWages;
    }

    /**
     * 获取 考勤扣款 字段:customer_payroll.attendance_deduction
     *
     * @return customer_payroll.attendance_deduction, 考勤扣款
     */
    public BigDecimal getAttendanceDeduction() {
        return attendanceDeduction;
    }

    /**
     * 设置 考勤扣款 字段:customer_payroll.attendance_deduction
     *
     * @param attendanceDeduction customer_payroll.attendance_deduction, 考勤扣款
     */
    public void setAttendanceDeduction(BigDecimal attendanceDeduction) {
        this.attendanceDeduction = attendanceDeduction;
    }

    /**
     * 获取 缺勤天数 字段:customer_payroll.absence_day_number
     *
     * @return customer_payroll.absence_day_number, 缺勤天数
     */
    public BigDecimal getAbsenceDayNumber() {
        return absenceDayNumber;
    }

    /**
     * 设置 缺勤天数 字段:customer_payroll.absence_day_number
     *
     * @param absenceDayNumber customer_payroll.absence_day_number, 缺勤天数
     */
    public void setAbsenceDayNumber(BigDecimal absenceDayNumber) {
        this.absenceDayNumber = absenceDayNumber;
    }

    /**
     * 获取 津贴总额 字段:customer_payroll.total_allowance
     *
     * @return customer_payroll.total_allowance, 津贴总额
     */
    public BigDecimal getTotalAllowance() {
        return totalAllowance;
    }

    /**
     * 设置 津贴总额 字段:customer_payroll.total_allowance
     *
     * @param totalAllowance customer_payroll.total_allowance, 津贴总额
     */
    public void setTotalAllowance(BigDecimal totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    /**
     * 获取 社保公积金 字段:customer_payroll.social_security_fund
     *
     * @return customer_payroll.social_security_fund, 社保公积金
     */
    public BigDecimal getSocialSecurityFund() {
        return socialSecurityFund;
    }

    /**
     * 设置 社保公积金 字段:customer_payroll.social_security_fund
     *
     * @param socialSecurityFund customer_payroll.social_security_fund, 社保公积金
     */
    public void setSocialSecurityFund(BigDecimal socialSecurityFund) {
        this.socialSecurityFund = socialSecurityFund;
    }

    /**
     * 获取 社保 字段:customer_payroll.social_security
     *
     * @return customer_payroll.social_security, 社保
     */
    public BigDecimal getSocialSecurity() {
        return socialSecurity;
    }

    /**
     * 设置 社保 字段:customer_payroll.social_security
     *
     * @param socialSecurity customer_payroll.social_security, 社保
     */
    public void setSocialSecurity(BigDecimal socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    /**
     * 获取 公积金 字段:customer_payroll.fund
     *
     * @return customer_payroll.fund, 公积金
     */
    public BigDecimal getFund() {
        return fund;
    }

    /**
     * 设置 公积金 字段:customer_payroll.fund
     *
     * @param fund customer_payroll.fund, 公积金
     */
    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    /**
     * 获取 税前补发/扣款 字段:customer_payroll.pretax
     *
     * @return customer_payroll.pretax, 税前补发/扣款
     */
    public BigDecimal getPretax() {
        return pretax;
    }

    /**
     * 设置 税前补发/扣款 字段:customer_payroll.pretax
     *
     * @param pretax customer_payroll.pretax, 税前补发/扣款
     */
    public void setPretax(BigDecimal pretax) {
        this.pretax = pretax;
    }

    /**
     * 获取 应发工资 字段:customer_payroll.should_amount
     *
     * @return customer_payroll.should_amount, 应发工资
     */
    public BigDecimal getShouldAmount() {
        return shouldAmount;
    }

    /**
     * 设置 应发工资 字段:customer_payroll.should_amount
     *
     * @param shouldAmount customer_payroll.should_amount, 应发工资
     */
    public void setShouldAmount(BigDecimal shouldAmount) {
        this.shouldAmount = shouldAmount;
    }

    /**
     * 获取 个税 字段:customer_payroll.personal_tax
     *
     * @return customer_payroll.personal_tax, 个税
     */
    public BigDecimal getPersonalTax() {
        return personalTax;
    }

    /**
     * 设置 个税 字段:customer_payroll.personal_tax
     *
     * @param personalTax customer_payroll.personal_tax, 个税
     */
    public void setPersonalTax(BigDecimal personalTax) {
        this.personalTax = personalTax;
    }

    /**
     * 获取 税后补发/扣款 字段:customer_payroll.after_tax
     *
     * @return customer_payroll.after_tax, 税后补发/扣款
     */
    public BigDecimal getAfterTax() {
        return afterTax;
    }

    /**
     * 设置 税后补发/扣款 字段:customer_payroll.after_tax
     *
     * @param afterTax customer_payroll.after_tax, 税后补发/扣款
     */
    public void setAfterTax(BigDecimal afterTax) {
        this.afterTax = afterTax;
    }

    /**
     * 获取 实发工资 字段:customer_payroll.real_wage
     *
     * @return customer_payroll.real_wage, 实发工资
     */
    public BigDecimal getRealWage() {
        return realWage;
    }

    /**
     * 设置 实发工资 字段:customer_payroll.real_wage
     *
     * @param realWage customer_payroll.real_wage, 实发工资
     */
    public void setRealWage(BigDecimal realWage) {
        this.realWage = realWage;
    }

    /**
     * 获取 创建时间 字段:customer_payroll.create_time
     *
     * @return customer_payroll.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:customer_payroll.create_time
     *
     * @param createTime customer_payroll.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 津贴设置是否被更新 0:未更新  1:已更新 字段:customer_payroll.is_allowance_update
     *
     * @return customer_payroll.is_allowance_update, 津贴设置是否被更新 0:未更新  1:已更新
     */
    public Integer getIsAllowanceUpdate() {
        return isAllowanceUpdate;
    }

    /**
     * 设置 津贴设置是否被更新 0:未更新  1:已更新 字段:customer_payroll.is_allowance_update
     *
     * @param isAllowanceUpdate customer_payroll.is_allowance_update, 津贴设置是否被更新 0:未更新  1:已更新
     */
    public void setIsAllowanceUpdate(Integer isAllowanceUpdate) {
        this.isAllowanceUpdate = isAllowanceUpdate;
    }

    /**
     * 获取 奖金设置是否被更新 0:未更新  1:已更新 字段:customer_payroll.is_bonus_update
     *
     * @return customer_payroll.is_bonus_update, 奖金设置是否被更新 0:未更新  1:已更新
     */
    public Integer getIsBonusUpdate() {
        return isBonusUpdate;
    }

    /**
     * 设置 奖金设置是否被更新 0:未更新  1:已更新 字段:customer_payroll.is_bonus_update
     *
     * @param isBonusUpdate customer_payroll.is_bonus_update, 奖金设置是否被更新 0:未更新  1:已更新
     */
    public void setIsBonusUpdate(Integer isBonusUpdate) {
        this.isBonusUpdate = isBonusUpdate;
    }

    /**
     * 获取 计薪规则是否被更新 0:未更新  1:已更新 字段:customer_payroll.is_payrule_update
     *
     * @return customer_payroll.is_payrule_update, 计薪规则是否被更新 0:未更新  1:已更新
     */
    public Integer getIsPayruleUpdate() {
        return isPayruleUpdate;
    }

    /**
     * 设置 计薪规则是否被更新 0:未更新  1:已更新 字段:customer_payroll.is_payrule_update
     *
     * @param isPayruleUpdate customer_payroll.is_payrule_update, 计薪规则是否被更新 0:未更新  1:已更新
     */
    public void setIsPayruleUpdate(Integer isPayruleUpdate) {
        this.isPayruleUpdate = isPayruleUpdate;
    }

    public Long getPayCycleId() {
        return payCycleId;
    }

    public void setPayCycleId(Long payCycleId) {
        this.payCycleId = payCycleId;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public Integer getIsPayOff() {
        return isPayOff;
    }

    public void setIsPayOff(Integer isPayOff) {
        this.isPayOff = isPayOff;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFinanceUser() {
        return financeUser;
    }

    public void setFinanceUser(String financeUser) {
        this.financeUser = financeUser;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public BigDecimal getWipedAmount() {
        return wipedAmount;
    }

    public void setWipedAmount(BigDecimal wipedAmount) {
        this.wipedAmount = wipedAmount;
    }

    public BigDecimal getAppraisalsSupplement() {
        return appraisalsSupplement;
    }

    public void setAppraisalsSupplement(BigDecimal appraisalsSupplement) {
        this.appraisalsSupplement = appraisalsSupplement;
    }

    public String getBonusJsonStr() {
        return bonusJsonStr;
    }

    public void setBonusJsonStr(String bonusJsonStr) {
        this.bonusJsonStr = bonusJsonStr;
    }

    public String getAllowanceJsonStr() {
        return allowanceJsonStr;
    }

    public void setAllowanceJsonStr(String allowanceJsonStr) {
        this.allowanceJsonStr = allowanceJsonStr;
    }

    public BigDecimal getSocialSecurityFundCorp() {
        return socialSecurityFundCorp;
    }

    public void setSocialSecurityFundCorp(BigDecimal socialSecurityFundCorp) {
        this.socialSecurityFundCorp = socialSecurityFundCorp;
    }
}