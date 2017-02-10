package com.xtr.api.domain.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomerSalarysBean implements Serializable {
    /**
     * id,所属表字段为customer_salarys.salary_id
     */
    private Long salaryId;

    /**
     * 企业工资文件id,所属表字段为customer_salarys.salary_excel_id
     */
    private Long salaryExcelId;

    /**
     * 企业id,所属表字段为customer_salarys.salary_company_id
     */
    private Long salaryCompanyId;

    /**
     * 子公司id ,所属表字段为customer_salarys.salary_dep_parentid
     */
    private Long salaryDepParentid;

    /**
     * 所在部门id(excel上传内容),所属表字段为customer_salarys.salary_dep_id
     */
    private Long salaryDepId;

    /**
     * 部门名称,所属表字段为customer_salarys.salary_dep_name
     */
    private String salaryDepName;

    /**
     * 用户id,所属表字段为customer_salarys.salary_customer_id
     */
    private Long salaryCustomerId;

    /**
     * 年度,所属表字段为customer_salarys.salary_year
     */
    private Integer salaryYear;

    /**
     * 月度,所属表字段为customer_salarys.salary_month
     */
    private Integer salaryMonth;

    /**
     * 薪资等级,所属表字段为customer_salarys.salary_level
     */
    private Integer salaryLevel;

    /**
     * 考勤天数,所属表字段为customer_salarys.salary_attendances_count
     */
    private Integer salaryAttendancesCount;

    /**
     * 应发工资,所属表字段为customer_salarys.salary_due
     */
    private BigDecimal salaryDue;

    /**
     * 基本工资,所属表字段为customer_salarys.salary_base
     */
    private BigDecimal salaryBase;

    /**
     * 绩效,所属表字段为customer_salarys.salary_achievements
     */
    private BigDecimal salaryAchievements;

    /**
     * 养老保险,所属表字段为customer_salarys.salary_pension
     */
    private BigDecimal salaryPension;

    /**
     * 医疗保险,所属表字段为customer_salarys.salary_health
     */
    private BigDecimal salaryHealth;

    /**
     * 失业保险,所属表字段为customer_salarys.salary_unemployed
     */
    private BigDecimal salaryUnemployed;

    /**
     * 工伤保险,所属表字段为customer_salarys.salary_industrial
     */
    private BigDecimal salaryIndustrial;

    /**
     * 生育保险,所属表字段为customer_salarys.salary_maternity
     */
    private BigDecimal salaryMaternity;

    /**
     * 住房公积金,所属表字段为customer_salarys.salary_housing
     */
    private BigDecimal salaryHousing;

    /**
     * 奖金,所属表字段为customer_salarys.salary_bonus
     */
    private BigDecimal salaryBonus;

    /**
     * 扣除,所属表字段为customer_salarys.salary_deduct
     */
    private BigDecimal salaryDeduct;

    /**
     * 个税,所属表字段为customer_salarys.salary_personal_tax
     */
    private BigDecimal salaryPersonalTax;

    /**
     * 实发工资,所属表字段为customer_salarys.salary_actual
     */
    private BigDecimal salaryActual;

    /**
     * 员工状态,所属表字段为customer_salarys.salary_customer_state
     */
    private String salaryCustomerState;

    /**
     * 公司成本,所属表字段为customer_salarys.salary_cost
     */
    private BigDecimal salaryCost;

    /**
     * 创建时间,所属表字段为customer_salarys.salary_addtime
     */
    private Date salaryAddtime;

    /**
     * 0不可用 1可用,所属表字段为customer_salarys.salary_sign
     */
    private Integer salarySign;

    /**
     * 业务标识,所属表字段为customer_salarys.tran_id
     */
    private Long tranId;

    /**
     * 0待审核 1初审失败 2初审成功 3复审失败 4复审成功 5审批失败 6审批成功 7工资已发 备注:(目前不清楚是以上传文档为单位还是以个人单位审核),所属表字段为customer_salarys.salary_state
     */
    private Integer salaryState;

    /**
     * 初审时间,所属表字段为customer_salarys.salary_audit_time_first
     */
    private Date salaryAuditTimeFirst;

    /**
     * 初审人,所属表字段为customer_salarys.salary_audit_member_first
     */
    private Long salaryAuditMemberFirst;

    /**
     * 初审备注,所属表字段为customer_salarys.salary_audit_remark_first
     */
    private String salaryAuditRemarkFirst;

    /**
     * 复审时间,所属表字段为customer_salarys.salary_audit_time_second
     */
    private Date salaryAuditTimeSecond;

    /**
     * 复审人,所属表字段为customer_salarys.salary_audit_member_second
     */
    private Long salaryAuditMemberSecond;

    /**
     * 复审备注,所属表字段为customer_salarys.salary_audit_remark_second
     */
    private String salaryAuditRemarkSecond;

    /**
     * 审批时间,所属表字段为customer_salarys.salary_audit_time_third
     */
    private Date salaryAuditTimeThird;

    /**
     * 审批人,所属表字段为customer_salarys.salary_audit_member_third
     */
    private Long salaryAuditMemberThird;

    /**
     * 审批备注,所属表字段为customer_salarys.salary_audit_remark_third
     */
    private String salaryAuditRemarkThird;

    /**
     * 病假(小时),所属表字段为customer_salarys.salary_sickleave_time
     */
    private Double salarySickleaveTime;

    /**
     * 事假(小时）,所属表字段为customer_salarys.salary_casualleave_time
     */
    private Double salaryCasualleaveTime;

    /**
     * 旷工(小时),所属表字段为customer_salarys.salary_absenteeism_time
     */
    private Double salaryAbsenteeismTime;

    /**
     * 缺勤(小时),所属表字段为customer_salarys.salary_absence_time
     */
    private Double salaryAbsenceTime;

    /**
     * 岗位津贴,所属表字段为customer_salarys.salary_postallowance
     */
    private BigDecimal salaryPostallowance;

    /**
     * 其他(加),所属表字段为customer_salarys.salary_otherplus
     */
    private BigDecimal salaryOtherplus;

    /**
     * 电话津贴,所属表字段为customer_salarys.salary_telallowance
     */
    private BigDecimal salaryTelallowance;

    /**
     * 车贴,所属表字段为customer_salarys.salary_carsticker
     */
    private BigDecimal salaryCarsticker;

    /**
     * 饭贴,所属表字段为customer_salarys.salary_ricepaste
     */
    private BigDecimal salaryRicepaste;

    /**
     * 病事假扣款,所属表字段为customer_salarys.salary_sickleave_money
     */
    private BigDecimal salarySickleaveMoney;

    /**
     * 其他(扣),所属表字段为customer_salarys.salary_otherbutton
     */
    private BigDecimal salaryOtherbutton;

    /**
     * 缺勤(旷工扣款),所属表字段为customer_salarys.salary_absence_money
     */
    private BigDecimal salaryAbsenceMoney;

    /**
     * 迟早退,所属表字段为customer_salarys.salary_lateearly
     */
    private BigDecimal salaryLateearly;

    /**
     * 扣税基数,所属表字段为customer_salarys.salary_taxbase
     */
    private BigDecimal salaryTaxbase;

    /**
     * 扣税数,所属表字段为customer_salarys.salary_taxdeduct
     */
    private BigDecimal salaryTaxdeduct;

    /**
     * 病假扣除,所属表字段为customer_salarys.salary_sickleave_deduct
     */
    private BigDecimal salarySickleaveDeduct;

    /**
     * 事假扣除,所属表字段为customer_salarys.salary_casualleave_deduct
     */
    private BigDecimal salaryCasualleaveDeduct;

    /**
     * 旷工扣除,所属表字段为customer_salarys.salary_absenteeism_deduct
     */
    private BigDecimal salaryAbsenteeismDeduct;

    /**
     * 缺勤扣除,所属表字段为customer_salarys.salary_absence_deduct
     */
    private BigDecimal salaryAbsenceDeduct;

    /**
     * 社保,所属表字段为customer_salarys.salary_social_security
     */
    private BigDecimal salarySocialSecurity;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 工资是否已发放  0:未发放  1:已发放
     */
    private Integer issue;

    /**
     * 工资单明细
     */
    private String salaryDetail;

    /**
     * 获取 id 字段:customer_salarys.salary_id
     *
     * @return customer_salarys.salary_id, id
     */
    public Long getSalaryId() {
        return salaryId;
    }

    /**
     * 设置 id 字段:customer_salarys.salary_id
     *
     * @param salaryId customer_salarys.salary_id, id
     */
    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }

    /**
     * 获取 企业工资文件id 字段:customer_salarys.salary_excel_id
     *
     * @return customer_salarys.salary_excel_id, 企业工资文件id
     */
    public Long getSalaryExcelId() {
        return salaryExcelId;
    }

    /**
     * 设置 企业工资文件id 字段:customer_salarys.salary_excel_id
     *
     * @param salaryExcelId customer_salarys.salary_excel_id, 企业工资文件id
     */
    public void setSalaryExcelId(Long salaryExcelId) {
        this.salaryExcelId = salaryExcelId;
    }

    /**
     * 获取 企业id 字段:customer_salarys.salary_company_id
     *
     * @return customer_salarys.salary_company_id, 企业id
     */
    public Long getSalaryCompanyId() {
        return salaryCompanyId;
    }

    /**
     * 设置 企业id 字段:customer_salarys.salary_company_id
     *
     * @param salaryCompanyId customer_salarys.salary_company_id, 企业id
     */
    public void setSalaryCompanyId(Long salaryCompanyId) {
        this.salaryCompanyId = salaryCompanyId;
    }

    /**
     * 获取 子公司id  字段:customer_salarys.salary_dep_parentid
     *
     * @return customer_salarys.salary_dep_parentid, 子公司id
     */
    public Long getSalaryDepParentid() {
        return salaryDepParentid;
    }

    /**
     * 设置 子公司id  字段:customer_salarys.salary_dep_parentid
     *
     * @param salaryDepParentid customer_salarys.salary_dep_parentid, 子公司id
     */
    public void setSalaryDepParentid(Long salaryDepParentid) {
        this.salaryDepParentid = salaryDepParentid;
    }

    /**
     * 获取 所在部门id(excel上传内容) 字段:customer_salarys.salary_dep_id
     *
     * @return customer_salarys.salary_dep_id, 所在部门id(excel上传内容)
     */
    public Long getSalaryDepId() {
        return salaryDepId;
    }

    /**
     * 设置 所在部门id(excel上传内容) 字段:customer_salarys.salary_dep_id
     *
     * @param salaryDepId customer_salarys.salary_dep_id, 所在部门id(excel上传内容)
     */
    public void setSalaryDepId(Long salaryDepId) {
        this.salaryDepId = salaryDepId;
    }

    /**
     * 获取 部门名称 字段:customer_salarys.salary_dep_name
     *
     * @return customer_salarys.salary_dep_name, 部门名称
     */
    public String getSalaryDepName() {
        return salaryDepName;
    }

    /**
     * 设置 部门名称 字段:customer_salarys.salary_dep_name
     *
     * @param salaryDepName customer_salarys.salary_dep_name, 部门名称
     */
    public void setSalaryDepName(String salaryDepName) {
        this.salaryDepName = salaryDepName == null ? null : salaryDepName.trim();
    }

    /**
     * 获取 用户id 字段:customer_salarys.salary_customer_id
     *
     * @return customer_salarys.salary_customer_id, 用户id
     */
    public Long getSalaryCustomerId() {
        return salaryCustomerId;
    }

    /**
     * 设置 用户id 字段:customer_salarys.salary_customer_id
     *
     * @param salaryCustomerId customer_salarys.salary_customer_id, 用户id
     */
    public void setSalaryCustomerId(Long salaryCustomerId) {
        this.salaryCustomerId = salaryCustomerId;
    }

    /**
     * 获取 年度 字段:customer_salarys.salary_year
     *
     * @return customer_salarys.salary_year, 年度
     */
    public Integer getSalaryYear() {
        return salaryYear;
    }

    /**
     * 设置 年度 字段:customer_salarys.salary_year
     *
     * @param salaryYear customer_salarys.salary_year, 年度
     */
    public void setSalaryYear(Integer salaryYear) {
        this.salaryYear = salaryYear;
    }

    /**
     * 获取 月度 字段:customer_salarys.salary_month
     *
     * @return customer_salarys.salary_month, 月度
     */
    public Integer getSalaryMonth() {
        return salaryMonth;
    }

    /**
     * 设置 月度 字段:customer_salarys.salary_month
     *
     * @param salaryMonth customer_salarys.salary_month, 月度
     */
    public void setSalaryMonth(Integer salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    /**
     * 获取 薪资等级 字段:customer_salarys.salary_level
     *
     * @return customer_salarys.salary_level, 薪资等级
     */
    public Integer getSalaryLevel() {
        return salaryLevel;
    }

    /**
     * 设置 薪资等级 字段:customer_salarys.salary_level
     *
     * @param salaryLevel customer_salarys.salary_level, 薪资等级
     */
    public void setSalaryLevel(Integer salaryLevel) {
        this.salaryLevel = salaryLevel;
    }

    /**
     * 获取 考勤天数 字段:customer_salarys.salary_attendances_count
     *
     * @return customer_salarys.salary_attendances_count, 考勤天数
     */
    public Integer getSalaryAttendancesCount() {
        return salaryAttendancesCount;
    }

    /**
     * 设置 考勤天数 字段:customer_salarys.salary_attendances_count
     *
     * @param salaryAttendancesCount customer_salarys.salary_attendances_count, 考勤天数
     */
    public void setSalaryAttendancesCount(Integer salaryAttendancesCount) {
        this.salaryAttendancesCount = salaryAttendancesCount;
    }

    /**
     * 获取 应发工资 字段:customer_salarys.salary_due
     *
     * @return customer_salarys.salary_due, 应发工资
     */
    public BigDecimal getSalaryDue() {
        return salaryDue;
    }

    /**
     * 设置 应发工资 字段:customer_salarys.salary_due
     *
     * @param salaryDue customer_salarys.salary_due, 应发工资
     */
    public void setSalaryDue(BigDecimal salaryDue) {
        this.salaryDue = salaryDue;
    }

    /**
     * 获取 基本工资 字段:customer_salarys.salary_base
     *
     * @return customer_salarys.salary_base, 基本工资
     */
    public BigDecimal getSalaryBase() {
        return salaryBase;
    }

    /**
     * 设置 基本工资 字段:customer_salarys.salary_base
     *
     * @param salaryBase customer_salarys.salary_base, 基本工资
     */
    public void setSalaryBase(BigDecimal salaryBase) {
        this.salaryBase = salaryBase;
    }

    /**
     * 获取 绩效 字段:customer_salarys.salary_achievements
     *
     * @return customer_salarys.salary_achievements, 绩效
     */
    public BigDecimal getSalaryAchievements() {
        return salaryAchievements;
    }

    /**
     * 设置 绩效 字段:customer_salarys.salary_achievements
     *
     * @param salaryAchievements customer_salarys.salary_achievements, 绩效
     */
    public void setSalaryAchievements(BigDecimal salaryAchievements) {
        this.salaryAchievements = salaryAchievements;
    }

    /**
     * 获取 养老保险 字段:customer_salarys.salary_pension
     *
     * @return customer_salarys.salary_pension, 养老保险
     */
    public BigDecimal getSalaryPension() {
        return salaryPension;
    }

    /**
     * 设置 养老保险 字段:customer_salarys.salary_pension
     *
     * @param salaryPension customer_salarys.salary_pension, 养老保险
     */
    public void setSalaryPension(BigDecimal salaryPension) {
        this.salaryPension = salaryPension;
    }

    /**
     * 获取 医疗保险 字段:customer_salarys.salary_health
     *
     * @return customer_salarys.salary_health, 医疗保险
     */
    public BigDecimal getSalaryHealth() {
        return salaryHealth;
    }

    /**
     * 设置 医疗保险 字段:customer_salarys.salary_health
     *
     * @param salaryHealth customer_salarys.salary_health, 医疗保险
     */
    public void setSalaryHealth(BigDecimal salaryHealth) {
        this.salaryHealth = salaryHealth;
    }

    /**
     * 获取 失业保险 字段:customer_salarys.salary_unemployed
     *
     * @return customer_salarys.salary_unemployed, 失业保险
     */
    public BigDecimal getSalaryUnemployed() {
        return salaryUnemployed;
    }

    /**
     * 设置 失业保险 字段:customer_salarys.salary_unemployed
     *
     * @param salaryUnemployed customer_salarys.salary_unemployed, 失业保险
     */
    public void setSalaryUnemployed(BigDecimal salaryUnemployed) {
        this.salaryUnemployed = salaryUnemployed;
    }

    /**
     * 获取 工伤保险 字段:customer_salarys.salary_industrial
     *
     * @return customer_salarys.salary_industrial, 工伤保险
     */
    public BigDecimal getSalaryIndustrial() {
        return salaryIndustrial;
    }

    /**
     * 设置 工伤保险 字段:customer_salarys.salary_industrial
     *
     * @param salaryIndustrial customer_salarys.salary_industrial, 工伤保险
     */
    public void setSalaryIndustrial(BigDecimal salaryIndustrial) {
        this.salaryIndustrial = salaryIndustrial;
    }

    /**
     * 获取 生育保险 字段:customer_salarys.salary_maternity
     *
     * @return customer_salarys.salary_maternity, 生育保险
     */
    public BigDecimal getSalaryMaternity() {
        return salaryMaternity;
    }

    /**
     * 设置 生育保险 字段:customer_salarys.salary_maternity
     *
     * @param salaryMaternity customer_salarys.salary_maternity, 生育保险
     */
    public void setSalaryMaternity(BigDecimal salaryMaternity) {
        this.salaryMaternity = salaryMaternity;
    }

    /**
     * 获取 住房公积金 字段:customer_salarys.salary_housing
     *
     * @return customer_salarys.salary_housing, 住房公积金
     */
    public BigDecimal getSalaryHousing() {
        return salaryHousing;
    }

    /**
     * 设置 住房公积金 字段:customer_salarys.salary_housing
     *
     * @param salaryHousing customer_salarys.salary_housing, 住房公积金
     */
    public void setSalaryHousing(BigDecimal salaryHousing) {
        this.salaryHousing = salaryHousing;
    }

    /**
     * 获取 奖金 字段:customer_salarys.salary_bonus
     *
     * @return customer_salarys.salary_bonus, 奖金
     */
    public BigDecimal getSalaryBonus() {
        return salaryBonus;
    }

    /**
     * 设置 奖金 字段:customer_salarys.salary_bonus
     *
     * @param salaryBonus customer_salarys.salary_bonus, 奖金
     */
    public void setSalaryBonus(BigDecimal salaryBonus) {
        this.salaryBonus = salaryBonus;
    }

    /**
     * 获取 扣除 字段:customer_salarys.salary_deduct
     *
     * @return customer_salarys.salary_deduct, 扣除
     */
    public BigDecimal getSalaryDeduct() {
        return salaryDeduct;
    }

    /**
     * 设置 扣除 字段:customer_salarys.salary_deduct
     *
     * @param salaryDeduct customer_salarys.salary_deduct, 扣除
     */
    public void setSalaryDeduct(BigDecimal salaryDeduct) {
        this.salaryDeduct = salaryDeduct;
    }

    /**
     * 获取 个税 字段:customer_salarys.salary_personal_tax
     *
     * @return customer_salarys.salary_personal_tax, 个税
     */
    public BigDecimal getSalaryPersonalTax() {
        return salaryPersonalTax;
    }

    /**
     * 设置 个税 字段:customer_salarys.salary_personal_tax
     *
     * @param salaryPersonalTax customer_salarys.salary_personal_tax, 个税
     */
    public void setSalaryPersonalTax(BigDecimal salaryPersonalTax) {
        this.salaryPersonalTax = salaryPersonalTax;
    }

    /**
     * 获取 实发工资 字段:customer_salarys.salary_actual
     *
     * @return customer_salarys.salary_actual, 实发工资
     */
    public BigDecimal getSalaryActual() {
        return salaryActual;
    }

    /**
     * 设置 实发工资 字段:customer_salarys.salary_actual
     *
     * @param salaryActual customer_salarys.salary_actual, 实发工资
     */
    public void setSalaryActual(BigDecimal salaryActual) {
        this.salaryActual = salaryActual;
    }

    /**
     * 获取 员工状态 字段:customer_salarys.salary_customer_state
     *
     * @return customer_salarys.salary_customer_state, 员工状态
     */
    public String getSalaryCustomerState() {
        return salaryCustomerState;
    }

    /**
     * 设置 员工状态 字段:customer_salarys.salary_customer_state
     *
     * @param salaryCustomerState customer_salarys.salary_customer_state, 员工状态
     */
    public void setSalaryCustomerState(String salaryCustomerState) {
        this.salaryCustomerState = salaryCustomerState == null ? null : salaryCustomerState.trim();
    }

    /**
     * 获取 公司成本 字段:customer_salarys.salary_cost
     *
     * @return customer_salarys.salary_cost, 公司成本
     */
    public BigDecimal getSalaryCost() {
        return salaryCost;
    }

    /**
     * 设置 公司成本 字段:customer_salarys.salary_cost
     *
     * @param salaryCost customer_salarys.salary_cost, 公司成本
     */
    public void setSalaryCost(BigDecimal salaryCost) {
        this.salaryCost = salaryCost;
    }

    /**
     * 获取 创建时间 字段:customer_salarys.salary_addtime
     *
     * @return customer_salarys.salary_addtime, 创建时间
     */
    public Date getSalaryAddtime() {
        return salaryAddtime;
    }

    /**
     * 设置 创建时间 字段:customer_salarys.salary_addtime
     *
     * @param salaryAddtime customer_salarys.salary_addtime, 创建时间
     */
    public void setSalaryAddtime(Date salaryAddtime) {
        this.salaryAddtime = salaryAddtime;
    }

    /**
     * 获取 0不可用 1可用 字段:customer_salarys.salary_sign
     *
     * @return customer_salarys.salary_sign, 0不可用 1可用
     */
    public Integer getSalarySign() {
        return salarySign;
    }

    /**
     * 设置 0不可用 1可用 字段:customer_salarys.salary_sign
     *
     * @param salarySign customer_salarys.salary_sign, 0不可用 1可用
     */
    public void setSalarySign(Integer salarySign) {
        this.salarySign = salarySign;
    }

    /**
     * 获取 业务标识 字段:customer_salarys.tran_id
     *
     * @return customer_salarys.tran_id, 业务标识
     */
    public Long getTranId() {
        return tranId;
    }

    /**
     * 设置 业务标识 字段:customer_salarys.tran_id
     *
     * @param tranId customer_salarys.tran_id, 业务标识
     */
    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    /**
     * 获取 0待审核 1初审失败 2初审成功 3复审失败 4复审成功 5审批失败 6审批成功 7工资已发 备注:(目前不清楚是以上传文档为单位还是以个人单位审核) 字段:customer_salarys.salary_state
     *
     * @return customer_salarys.salary_state, 0待审核 1初审失败 2初审成功 3复审失败 4复审成功 5审批失败 6审批成功 7工资已发 备注:(目前不清楚是以上传文档为单位还是以个人单位审核)
     */
    public Integer getSalaryState() {
        return salaryState;
    }

    /**
     * 设置 0待审核 1初审失败 2初审成功 3复审失败 4复审成功 5审批失败 6审批成功 7工资已发 备注:(目前不清楚是以上传文档为单位还是以个人单位审核) 字段:customer_salarys.salary_state
     *
     * @param salaryState customer_salarys.salary_state, 0待审核 1初审失败 2初审成功 3复审失败 4复审成功 5审批失败 6审批成功 7工资已发 备注:(目前不清楚是以上传文档为单位还是以个人单位审核)
     */
    public void setSalaryState(Integer salaryState) {
        this.salaryState = salaryState;
    }

    /**
     * 获取 初审时间 字段:customer_salarys.salary_audit_time_first
     *
     * @return customer_salarys.salary_audit_time_first, 初审时间
     */
    public Date getSalaryAuditTimeFirst() {
        return salaryAuditTimeFirst;
    }

    /**
     * 设置 初审时间 字段:customer_salarys.salary_audit_time_first
     *
     * @param salaryAuditTimeFirst customer_salarys.salary_audit_time_first, 初审时间
     */
    public void setSalaryAuditTimeFirst(Date salaryAuditTimeFirst) {
        this.salaryAuditTimeFirst = salaryAuditTimeFirst;
    }

    /**
     * 获取 初审人 字段:customer_salarys.salary_audit_member_first
     *
     * @return customer_salarys.salary_audit_member_first, 初审人
     */
    public Long getSalaryAuditMemberFirst() {
        return salaryAuditMemberFirst;
    }

    /**
     * 设置 初审人 字段:customer_salarys.salary_audit_member_first
     *
     * @param salaryAuditMemberFirst customer_salarys.salary_audit_member_first, 初审人
     */
    public void setSalaryAuditMemberFirst(Long salaryAuditMemberFirst) {
        this.salaryAuditMemberFirst = salaryAuditMemberFirst;
    }

    /**
     * 获取 初审备注 字段:customer_salarys.salary_audit_remark_first
     *
     * @return customer_salarys.salary_audit_remark_first, 初审备注
     */
    public String getSalaryAuditRemarkFirst() {
        return salaryAuditRemarkFirst;
    }

    /**
     * 设置 初审备注 字段:customer_salarys.salary_audit_remark_first
     *
     * @param salaryAuditRemarkFirst customer_salarys.salary_audit_remark_first, 初审备注
     */
    public void setSalaryAuditRemarkFirst(String salaryAuditRemarkFirst) {
        this.salaryAuditRemarkFirst = salaryAuditRemarkFirst == null ? null : salaryAuditRemarkFirst.trim();
    }

    /**
     * 获取 复审时间 字段:customer_salarys.salary_audit_time_second
     *
     * @return customer_salarys.salary_audit_time_second, 复审时间
     */
    public Date getSalaryAuditTimeSecond() {
        return salaryAuditTimeSecond;
    }

    /**
     * 设置 复审时间 字段:customer_salarys.salary_audit_time_second
     *
     * @param salaryAuditTimeSecond customer_salarys.salary_audit_time_second, 复审时间
     */
    public void setSalaryAuditTimeSecond(Date salaryAuditTimeSecond) {
        this.salaryAuditTimeSecond = salaryAuditTimeSecond;
    }

    /**
     * 获取 复审人 字段:customer_salarys.salary_audit_member_second
     *
     * @return customer_salarys.salary_audit_member_second, 复审人
     */
    public Long getSalaryAuditMemberSecond() {
        return salaryAuditMemberSecond;
    }

    /**
     * 设置 复审人 字段:customer_salarys.salary_audit_member_second
     *
     * @param salaryAuditMemberSecond customer_salarys.salary_audit_member_second, 复审人
     */
    public void setSalaryAuditMemberSecond(Long salaryAuditMemberSecond) {
        this.salaryAuditMemberSecond = salaryAuditMemberSecond;
    }

    /**
     * 获取 复审备注 字段:customer_salarys.salary_audit_remark_second
     *
     * @return customer_salarys.salary_audit_remark_second, 复审备注
     */
    public String getSalaryAuditRemarkSecond() {
        return salaryAuditRemarkSecond;
    }

    /**
     * 设置 复审备注 字段:customer_salarys.salary_audit_remark_second
     *
     * @param salaryAuditRemarkSecond customer_salarys.salary_audit_remark_second, 复审备注
     */
    public void setSalaryAuditRemarkSecond(String salaryAuditRemarkSecond) {
        this.salaryAuditRemarkSecond = salaryAuditRemarkSecond == null ? null : salaryAuditRemarkSecond.trim();
    }

    /**
     * 获取 审批时间 字段:customer_salarys.salary_audit_time_third
     *
     * @return customer_salarys.salary_audit_time_third, 审批时间
     */
    public Date getSalaryAuditTimeThird() {
        return salaryAuditTimeThird;
    }

    /**
     * 设置 审批时间 字段:customer_salarys.salary_audit_time_third
     *
     * @param salaryAuditTimeThird customer_salarys.salary_audit_time_third, 审批时间
     */
    public void setSalaryAuditTimeThird(Date salaryAuditTimeThird) {
        this.salaryAuditTimeThird = salaryAuditTimeThird;
    }

    /**
     * 获取 审批人 字段:customer_salarys.salary_audit_member_third
     *
     * @return customer_salarys.salary_audit_member_third, 审批人
     */
    public Long getSalaryAuditMemberThird() {
        return salaryAuditMemberThird;
    }

    /**
     * 设置 审批人 字段:customer_salarys.salary_audit_member_third
     *
     * @param salaryAuditMemberThird customer_salarys.salary_audit_member_third, 审批人
     */
    public void setSalaryAuditMemberThird(Long salaryAuditMemberThird) {
        this.salaryAuditMemberThird = salaryAuditMemberThird;
    }

    /**
     * 获取 审批备注 字段:customer_salarys.salary_audit_remark_third
     *
     * @return customer_salarys.salary_audit_remark_third, 审批备注
     */
    public String getSalaryAuditRemarkThird() {
        return salaryAuditRemarkThird;
    }

    /**
     * 设置 审批备注 字段:customer_salarys.salary_audit_remark_third
     *
     * @param salaryAuditRemarkThird customer_salarys.salary_audit_remark_third, 审批备注
     */
    public void setSalaryAuditRemarkThird(String salaryAuditRemarkThird) {
        this.salaryAuditRemarkThird = salaryAuditRemarkThird == null ? null : salaryAuditRemarkThird.trim();
    }

    /**
     * 获取 病假(小时) 字段:customer_salarys.salary_sickleave_time
     *
     * @return customer_salarys.salary_sickleave_time, 病假(小时)
     */
    public Double getSalarySickleaveTime() {
        return salarySickleaveTime;
    }

    /**
     * 设置 病假(小时) 字段:customer_salarys.salary_sickleave_time
     *
     * @param salarySickleaveTime customer_salarys.salary_sickleave_time, 病假(小时)
     */
    public void setSalarySickleaveTime(Double salarySickleaveTime) {
        this.salarySickleaveTime = salarySickleaveTime;
    }

    /**
     * 获取 事假(小时） 字段:customer_salarys.salary_casualleave_time
     *
     * @return customer_salarys.salary_casualleave_time, 事假(小时）
     */
    public Double getSalaryCasualleaveTime() {
        return salaryCasualleaveTime;
    }

    /**
     * 设置 事假(小时） 字段:customer_salarys.salary_casualleave_time
     *
     * @param salaryCasualleaveTime customer_salarys.salary_casualleave_time, 事假(小时）
     */
    public void setSalaryCasualleaveTime(Double salaryCasualleaveTime) {
        this.salaryCasualleaveTime = salaryCasualleaveTime;
    }

    /**
     * 获取 旷工(小时) 字段:customer_salarys.salary_absenteeism_time
     *
     * @return customer_salarys.salary_absenteeism_time, 旷工(小时)
     */
    public Double getSalaryAbsenteeismTime() {
        return salaryAbsenteeismTime;
    }

    /**
     * 设置 旷工(小时) 字段:customer_salarys.salary_absenteeism_time
     *
     * @param salaryAbsenteeismTime customer_salarys.salary_absenteeism_time, 旷工(小时)
     */
    public void setSalaryAbsenteeismTime(Double salaryAbsenteeismTime) {
        this.salaryAbsenteeismTime = salaryAbsenteeismTime;
    }

    /**
     * 获取 缺勤(小时) 字段:customer_salarys.salary_absence_time
     *
     * @return customer_salarys.salary_absence_time, 缺勤(小时)
     */
    public Double getSalaryAbsenceTime() {
        return salaryAbsenceTime;
    }

    /**
     * 设置 缺勤(小时) 字段:customer_salarys.salary_absence_time
     *
     * @param salaryAbsenceTime customer_salarys.salary_absence_time, 缺勤(小时)
     */
    public void setSalaryAbsenceTime(Double salaryAbsenceTime) {
        this.salaryAbsenceTime = salaryAbsenceTime;
    }

    /**
     * 获取 岗位津贴 字段:customer_salarys.salary_postallowance
     *
     * @return customer_salarys.salary_postallowance, 岗位津贴
     */
    public BigDecimal getSalaryPostallowance() {
        return salaryPostallowance;
    }

    /**
     * 设置 岗位津贴 字段:customer_salarys.salary_postallowance
     *
     * @param salaryPostallowance customer_salarys.salary_postallowance, 岗位津贴
     */
    public void setSalaryPostallowance(BigDecimal salaryPostallowance) {
        this.salaryPostallowance = salaryPostallowance;
    }

    /**
     * 获取 其他(加) 字段:customer_salarys.salary_otherplus
     *
     * @return customer_salarys.salary_otherplus, 其他(加)
     */
    public BigDecimal getSalaryOtherplus() {
        return salaryOtherplus;
    }

    /**
     * 设置 其他(加) 字段:customer_salarys.salary_otherplus
     *
     * @param salaryOtherplus customer_salarys.salary_otherplus, 其他(加)
     */
    public void setSalaryOtherplus(BigDecimal salaryOtherplus) {
        this.salaryOtherplus = salaryOtherplus;
    }

    /**
     * 获取 电话津贴 字段:customer_salarys.salary_telallowance
     *
     * @return customer_salarys.salary_telallowance, 电话津贴
     */
    public BigDecimal getSalaryTelallowance() {
        return salaryTelallowance;
    }

    /**
     * 设置 电话津贴 字段:customer_salarys.salary_telallowance
     *
     * @param salaryTelallowance customer_salarys.salary_telallowance, 电话津贴
     */
    public void setSalaryTelallowance(BigDecimal salaryTelallowance) {
        this.salaryTelallowance = salaryTelallowance;
    }

    /**
     * 获取 车贴 字段:customer_salarys.salary_carsticker
     *
     * @return customer_salarys.salary_carsticker, 车贴
     */
    public BigDecimal getSalaryCarsticker() {
        return salaryCarsticker;
    }

    /**
     * 设置 车贴 字段:customer_salarys.salary_carsticker
     *
     * @param salaryCarsticker customer_salarys.salary_carsticker, 车贴
     */
    public void setSalaryCarsticker(BigDecimal salaryCarsticker) {
        this.salaryCarsticker = salaryCarsticker;
    }

    /**
     * 获取 饭贴 字段:customer_salarys.salary_ricepaste
     *
     * @return customer_salarys.salary_ricepaste, 饭贴
     */
    public BigDecimal getSalaryRicepaste() {
        return salaryRicepaste;
    }

    /**
     * 设置 饭贴 字段:customer_salarys.salary_ricepaste
     *
     * @param salaryRicepaste customer_salarys.salary_ricepaste, 饭贴
     */
    public void setSalaryRicepaste(BigDecimal salaryRicepaste) {
        this.salaryRicepaste = salaryRicepaste;
    }

    /**
     * 获取 病事假扣款 字段:customer_salarys.salary_sickleave_money
     *
     * @return customer_salarys.salary_sickleave_money, 病事假扣款
     */
    public BigDecimal getSalarySickleaveMoney() {
        return salarySickleaveMoney;
    }

    /**
     * 设置 病事假扣款 字段:customer_salarys.salary_sickleave_money
     *
     * @param salarySickleaveMoney customer_salarys.salary_sickleave_money, 病事假扣款
     */
    public void setSalarySickleaveMoney(BigDecimal salarySickleaveMoney) {
        this.salarySickleaveMoney = salarySickleaveMoney;
    }

    /**
     * 获取 其他(扣) 字段:customer_salarys.salary_otherbutton
     *
     * @return customer_salarys.salary_otherbutton, 其他(扣)
     */
    public BigDecimal getSalaryOtherbutton() {
        return salaryOtherbutton;
    }

    /**
     * 设置 其他(扣) 字段:customer_salarys.salary_otherbutton
     *
     * @param salaryOtherbutton customer_salarys.salary_otherbutton, 其他(扣)
     */
    public void setSalaryOtherbutton(BigDecimal salaryOtherbutton) {
        this.salaryOtherbutton = salaryOtherbutton;
    }

    /**
     * 获取 缺勤(旷工扣款) 字段:customer_salarys.salary_absence_money
     *
     * @return customer_salarys.salary_absence_money, 缺勤(旷工扣款)
     */
    public BigDecimal getSalaryAbsenceMoney() {
        return salaryAbsenceMoney;
    }

    /**
     * 设置 缺勤(旷工扣款) 字段:customer_salarys.salary_absence_money
     *
     * @param salaryAbsenceMoney customer_salarys.salary_absence_money, 缺勤(旷工扣款)
     */
    public void setSalaryAbsenceMoney(BigDecimal salaryAbsenceMoney) {
        this.salaryAbsenceMoney = salaryAbsenceMoney;
    }

    /**
     * 获取 迟早退 字段:customer_salarys.salary_lateearly
     *
     * @return customer_salarys.salary_lateearly, 迟早退
     */
    public BigDecimal getSalaryLateearly() {
        return salaryLateearly;
    }

    /**
     * 设置 迟早退 字段:customer_salarys.salary_lateearly
     *
     * @param salaryLateearly customer_salarys.salary_lateearly, 迟早退
     */
    public void setSalaryLateearly(BigDecimal salaryLateearly) {
        this.salaryLateearly = salaryLateearly;
    }

    /**
     * 获取 扣税基数 字段:customer_salarys.salary_taxbase
     *
     * @return customer_salarys.salary_taxbase, 扣税基数
     */
    public BigDecimal getSalaryTaxbase() {
        return salaryTaxbase;
    }

    /**
     * 设置 扣税基数 字段:customer_salarys.salary_taxbase
     *
     * @param salaryTaxbase customer_salarys.salary_taxbase, 扣税基数
     */
    public void setSalaryTaxbase(BigDecimal salaryTaxbase) {
        this.salaryTaxbase = salaryTaxbase;
    }

    /**
     * 获取 扣税数 字段:customer_salarys.salary_taxdeduct
     *
     * @return customer_salarys.salary_taxdeduct, 扣税数
     */
    public BigDecimal getSalaryTaxdeduct() {
        return salaryTaxdeduct;
    }

    /**
     * 设置 扣税数 字段:customer_salarys.salary_taxdeduct
     *
     * @param salaryTaxdeduct customer_salarys.salary_taxdeduct, 扣税数
     */
    public void setSalaryTaxdeduct(BigDecimal salaryTaxdeduct) {
        this.salaryTaxdeduct = salaryTaxdeduct;
    }

    /**
     * 获取 病假扣除 字段:customer_salarys.salary_sickleave_deduct
     *
     * @return customer_salarys.salary_sickleave_deduct, 病假扣除
     */
    public BigDecimal getSalarySickleaveDeduct() {
        return salarySickleaveDeduct;
    }

    /**
     * 设置 病假扣除 字段:customer_salarys.salary_sickleave_deduct
     *
     * @param salarySickleaveDeduct customer_salarys.salary_sickleave_deduct, 病假扣除
     */
    public void setSalarySickleaveDeduct(BigDecimal salarySickleaveDeduct) {
        this.salarySickleaveDeduct = salarySickleaveDeduct;
    }

    /**
     * 获取 事假扣除 字段:customer_salarys.salary_casualleave_deduct
     *
     * @return customer_salarys.salary_casualleave_deduct, 事假扣除
     */
    public BigDecimal getSalaryCasualleaveDeduct() {
        return salaryCasualleaveDeduct;
    }

    /**
     * 设置 事假扣除 字段:customer_salarys.salary_casualleave_deduct
     *
     * @param salaryCasualleaveDeduct customer_salarys.salary_casualleave_deduct, 事假扣除
     */
    public void setSalaryCasualleaveDeduct(BigDecimal salaryCasualleaveDeduct) {
        this.salaryCasualleaveDeduct = salaryCasualleaveDeduct;
    }

    /**
     * 获取 旷工扣除 字段:customer_salarys.salary_absenteeism_deduct
     *
     * @return customer_salarys.salary_absenteeism_deduct, 旷工扣除
     */
    public BigDecimal getSalaryAbsenteeismDeduct() {
        return salaryAbsenteeismDeduct;
    }

    /**
     * 设置 旷工扣除 字段:customer_salarys.salary_absenteeism_deduct
     *
     * @param salaryAbsenteeismDeduct customer_salarys.salary_absenteeism_deduct, 旷工扣除
     */
    public void setSalaryAbsenteeismDeduct(BigDecimal salaryAbsenteeismDeduct) {
        this.salaryAbsenteeismDeduct = salaryAbsenteeismDeduct;
    }

    /**
     * 获取 缺勤扣除 字段:customer_salarys.salary_absence_deduct
     *
     * @return customer_salarys.salary_absence_deduct, 缺勤扣除
     */
    public BigDecimal getSalaryAbsenceDeduct() {
        return salaryAbsenceDeduct;
    }

    /**
     * 设置 缺勤扣除 字段:customer_salarys.salary_absence_deduct
     *
     * @param salaryAbsenceDeduct customer_salarys.salary_absence_deduct, 缺勤扣除
     */
    public void setSalaryAbsenceDeduct(BigDecimal salaryAbsenceDeduct) {
        this.salaryAbsenceDeduct = salaryAbsenceDeduct;
    }

    /**
     * 获取 社保 字段:customer_salarys.salary_social_security
     *
     * @return customer_salarys.salary_social_security, 社保
     */
    public BigDecimal getSalarySocialSecurity() {
        return salarySocialSecurity;
    }

    /**
     * 设置 社保 字段:customer_salarys.salary_social_security
     *
     * @param salarySocialSecurity customer_salarys.salary_social_security, 社保
     */
    public void setSalarySocialSecurity(BigDecimal salarySocialSecurity) {
        this.salarySocialSecurity = salarySocialSecurity;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public String getSalaryDetail() {
        return salaryDetail;
    }

    public void setSalaryDetail(String salaryDetail) {
        this.salaryDetail = salaryDetail;
    }
}