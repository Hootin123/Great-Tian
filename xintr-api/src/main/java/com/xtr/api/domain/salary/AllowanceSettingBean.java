package com.xtr.api.domain.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * 津贴设置表
 */
public class AllowanceSettingBean  extends BaseObject implements Serializable {
    /**
     * 主键,所属表字段为allowance_setting.id
     */
    private Long id;

    /**
     * 公司id,所属表字段为allowance_setting.company_id
     */
    private Long companyId;

    /**
     * 部门Id,所属表字段为allowance_setting.dept_id
     */
    private Long deptId;

    /**
     * 企业用户Id,所属表字段为allowance_setting.company_menber_id
     */
    private Long companyMenberId;

    /**
     * 津贴名称,所属表字段为allowance_setting.allowance_name
     */
    private String allowanceName;

    /**
     * 津贴备注
     */
    private String allowanceDesc;

    /**
     * 是否启用适用范围 0:否  1:是,所属表字段为allowance_setting.is_apply_range
     */
    private Integer isApplyRange;

    /**
     * 缺勤天数大于时，不享受该津贴,所属表字段为allowance_setting.absence_day
     */
    private Integer absenceDay;

    /**
     * 金额,所属表字段为allowance_setting.amount
     */
    private BigDecimal amount;

    /**
     * 津贴方式  0:每月 1:每出勤日,所属表字段为allowance_setting.allowance_type
     */
    private Integer allowanceType;

    /**
     * 正式转正 0:不适合 1:适合,所属表字段为allowance_setting.formally_promotion
     */
    private Integer formallyPromotion;

    /**
     * 正式试用期 0:不适合 1:适合,所属表字段为allowance_setting.formally_probation_period
     */
    private Integer formallyProbationPeriod;

    /**
     * 劳务转正 0:不适合 1:适合,所属表字段为allowance_setting.labor_promotion
     */
    private Integer laborPromotion;

    /**
     * 劳务试用期 0:不适合 1:适合
     */
    private Integer laborPromotionPeriod;

    /**
     * 创建时间,所属表字段为allowance_setting.create_time
     */
    private Date createTime;

    /**
     * 获取 主键 字段:allowance_setting.id
     *
     * @return allowance_setting.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:allowance_setting.id
     *
     * @param id allowance_setting.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 公司id 字段:allowance_setting.company_id
     *
     * @return allowance_setting.company_id, 公司id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 公司id 字段:allowance_setting.company_id
     *
     * @param companyId allowance_setting.company_id, 公司id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 部门Id 字段:allowance_setting.dept_id
     *
     * @return allowance_setting.dept_id, 部门Id
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置 部门Id 字段:allowance_setting.dept_id
     *
     * @param deptId allowance_setting.dept_id, 部门Id
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取 企业用户Id 字段:allowance_setting.company_menber_id
     *
     * @return allowance_setting.company_menber_id, 企业用户Id
     */
    public Long getCompanyMenberId() {
        return companyMenberId;
    }

    /**
     * 设置 企业用户Id 字段:allowance_setting.company_menber_id
     *
     * @param companyMenberId allowance_setting.company_menber_id, 企业用户Id
     */
    public void setCompanyMenberId(Long companyMenberId) {
        this.companyMenberId = companyMenberId;
    }

    /**
     * 获取 津贴名称 字段:allowance_setting.allowance_name
     *
     * @return allowance_setting.allowance_name, 津贴名称
     */
    public String getAllowanceName() {
        return allowanceName;
    }

    /**
     * 设置 津贴名称 字段:allowance_setting.allowance_name
     *
     * @param allowanceName allowance_setting.allowance_name, 津贴名称
     */
    public void setAllowanceName(String allowanceName) {
        this.allowanceName = allowanceName == null ? null : allowanceName.trim();
    }

    /**
     * 获取 是否启用适用范围 0:否  1:是 字段:allowance_setting.is_apply_range
     *
     * @return allowance_setting.is_apply_range, 是否启用适用范围 0:否  1:是
     */
    public Integer getIsApplyRange() {
        return isApplyRange;
    }

    /**
     * 设置 是否启用适用范围 0:否  1:是 字段:allowance_setting.is_apply_range
     *
     * @param isApplyRange allowance_setting.is_apply_range, 是否启用适用范围 0:否  1:是
     */
    public void setIsApplyRange(Integer isApplyRange) {
        this.isApplyRange = isApplyRange;
    }

    /**
     * 获取 缺勤天数大于时，不享受该津贴 字段:allowance_setting.absence_day
     *
     * @return allowance_setting.absence_day, 缺勤天数大于时，不享受该津贴
     */
    public Integer getAbsenceDay() {
        return absenceDay;
    }

    /**
     * 设置 缺勤天数大于时，不享受该津贴 字段:allowance_setting.absence_day
     *
     * @param absenceDay allowance_setting.absence_day, 缺勤天数大于时，不享受该津贴
     */
    public void setAbsenceDay(Integer absenceDay) {
        this.absenceDay = absenceDay;
    }

    /**
     * 获取 金额 字段:allowance_setting.amount
     *
     * @return allowance_setting.amount, 金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置 金额 字段:allowance_setting.amount
     *
     * @param amount allowance_setting.amount, 金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取 津贴方式  0:每月 1:每出勤日 字段:allowance_setting.allowance_type
     *
     * @return allowance_setting.allowance_type, 津贴方式  0:每月 1:每出勤日
     */
    public Integer getAllowanceType() {
        return allowanceType;
    }

    /**
     * 设置 津贴方式  0:每月 1:每出勤日 字段:allowance_setting.allowance_type
     *
     * @param allowanceType allowance_setting.allowance_type, 津贴方式  0:每月 1:每出勤日
     */
    public void setAllowanceType(Integer allowanceType) {
        this.allowanceType = allowanceType;
    }

    /**
     * 获取 正式转正 0:不适合 1:适合 字段:allowance_setting.formally_promotion
     *
     * @return allowance_setting.formally_promotion, 正式转正 0:不适合 1:适合
     */
    public Integer getFormallyPromotion() {
        return formallyPromotion;
    }

    /**
     * 设置 正式转正 0:不适合 1:适合 字段:allowance_setting.formally_promotion
     *
     * @param formallyPromotion allowance_setting.formally_promotion, 正式转正 0:不适合 1:适合
     */
    public void setFormallyPromotion(Integer formallyPromotion) {
        this.formallyPromotion = formallyPromotion;
    }

    /**
     * 获取 正式试用期 0:不适合 1:适合 字段:allowance_setting.formally_probation_period
     *
     * @return allowance_setting.formally_probation_period, 正式试用期 0:不适合 1:适合
     */
    public Integer getFormallyProbationPeriod() {
        return formallyProbationPeriod;
    }

    /**
     * 设置 正式试用期 0:不适合 1:适合 字段:allowance_setting.formally_probation_period
     *
     * @param formallyProbationPeriod allowance_setting.formally_probation_period, 正式试用期 0:不适合 1:适合
     */
    public void setFormallyProbationPeriod(Integer formallyProbationPeriod) {
        this.formallyProbationPeriod = formallyProbationPeriod;
    }

    /**
     * 获取 劳务转正 0:不适合 1:适合 字段:allowance_setting.labor_promotion
     *
     * @return allowance_setting.labor_promotion, 劳务转正 0:不适合 1:适合
     */
    public Integer getLaborPromotion() {
        return laborPromotion;
    }

    /**
     * 设置 劳务转正 0:不适合 1:适合 字段:allowance_setting.labor_promotion
     *
     * @param laborPromotion allowance_setting.labor_promotion, 劳务转正 0:不适合 1:适合
     */
    public void setLaborPromotion(Integer laborPromotion) {
        this.laborPromotion = laborPromotion;
    }

    /**
     * 获取 创建时间 字段:allowance_setting.create_time
     *
     * @return allowance_setting.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:allowance_setting.create_time
     *
     * @param createTime allowance_setting.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAllowanceDesc() {
        return allowanceDesc;
    }

    public void setAllowanceDesc(String allowanceDesc) {
        this.allowanceDesc = allowanceDesc;
    }

    public Integer getLaborPromotionPeriod() {
        return laborPromotionPeriod;
    }

    public void setLaborPromotionPeriod(Integer laborPromotionPeriod) {
        this.laborPromotionPeriod = laborPromotionPeriod;
    }
}