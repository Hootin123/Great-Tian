package com.xtr.api.domain.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 奖金设置表
 */
public class BonusSettingsBean extends BaseObject implements Serializable {
    /**
     * 主键,所属表字段为bonus_settings.id
     */
    private Long id;

    /**
     * 公司id,所属表字段为bonus_settings.company_id
     */
    private Long companyId;

    /**
     * 部门Id,所属表字段为bonus_settings.dept_id
     */
    private Long deptId;

    /**
     * 企业用户Id,所属表字段为bonus_settings.company_menber_id
     */
    private Long companyMenberId;

    /**
     * 奖金名称,所属表字段为bonus_settings.bonus_name
     */
    private String bonusName;

    /**
     * 创建时间,所属表字段为bonus_settings.create_time
     */
    private Date createTime;

    /**
     * 获取 主键 字段:bonus_settings.id
     *
     * @return bonus_settings.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:bonus_settings.id
     *
     * @param id bonus_settings.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 公司id 字段:bonus_settings.company_id
     *
     * @return bonus_settings.company_id, 公司id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 公司id 字段:bonus_settings.company_id
     *
     * @param companyId bonus_settings.company_id, 公司id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 部门Id 字段:bonus_settings.dept_id
     *
     * @return bonus_settings.dept_id, 部门Id
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置 部门Id 字段:bonus_settings.dept_id
     *
     * @param deptId bonus_settings.dept_id, 部门Id
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取 企业用户Id 字段:bonus_settings.company_menber_id
     *
     * @return bonus_settings.company_menber_id, 企业用户Id
     */
    public Long getCompanyMenberId() {
        return companyMenberId;
    }

    /**
     * 设置 企业用户Id 字段:bonus_settings.company_menber_id
     *
     * @param companyMenberId bonus_settings.company_menber_id, 企业用户Id
     */
    public void setCompanyMenberId(Long companyMenberId) {
        this.companyMenberId = companyMenberId;
    }

    /**
     * 获取 奖金名称 字段:bonus_settings.bonus_name
     *
     * @return bonus_settings.bonus_name, 奖金名称
     */
    public String getBonusName() {
        return bonusName;
    }

    /**
     * 设置 奖金名称 字段:bonus_settings.bonus_name
     *
     * @param bonusName bonus_settings.bonus_name, 奖金名称
     */
    public void setBonusName(String bonusName) {
        this.bonusName = bonusName == null ? null : bonusName.trim();
    }

    /**
     * 获取 创建时间 字段:bonus_settings.create_time
     *
     * @return bonus_settings.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:bonus_settings.create_time
     *
     * @param createTime bonus_settings.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}