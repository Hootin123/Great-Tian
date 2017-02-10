package com.xtr.api.domain.company;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公司与部门信息
 */
public class CompanyDepsBean implements Serializable {
    /**
     * id,所属表字段为company_deps.dep_id
     */
    private Long depId;

    /**
     * 所属企业id,所属表字段为company_deps.dep_company_id
     */
    private Long depCompanyId;

    /**
     * 上级id,所属表字段为company_deps.dep_parent_id
     */
    private Long depParentId;

    /**
     * 本门名称,所属表字段为company_deps.dep_name
     */
    private String depName;

    /**
     * 部门编号,所属表字段为company_deps.dep_number
     */
    private String depNumber;

    /**
     * 1总公司(集团) 2部门 3子公司,所属表字段为company_deps.dep_type
     */
    private Integer depType;

    /**
     * 发薪日 0未设置,所属表字段为company_deps.dep_salary_day
     */
    private Integer depSalaryDay;

    /**
     * 状态 0禁用 1可用,所属表字段为company_deps.dep_sign
     */
    private Integer depSign;

    /**
     * 薪资计算周期,所属表字段为company_deps.dep_salary_sumdays
     */
    private Integer depSalarySumdays;

    /**
     * 员工上传日,所属表字段为company_deps.dep_updateuser_date
     */
    private Integer depUpdateuserDate;

    /**
     * 修改人,所属表字段为company_deps.dep_edit_member
     */
    private Long depEditMember;

    /**
     * 修改时间,所属表字段为company_deps.dep_edit_time
     */
    private Date depEditTime;

    /**
     * 企业id,所属表字段为company_deps.company_id
     */
    private Long companyId;

    /**
     * 所属层级 1 开始,所属表字段为company_deps.dep_level
     */
    private Integer depLevel;

    /**
     * 当前部门员工数量
     */
    private Integer depCustCount;

    /**
     * 部门负责人
     */
    private Long depLeader;

    private Integer delflag;

    private List<CompanyDepsBean> children;

    private String leaderName;

    /**
     * 获取 id 字段:company_deps.dep_id
     *
     * @return company_deps.dep_id, id
     */
    public Long getDepId() {
        return depId;
    }

    /**
     * 设置 id 字段:company_deps.dep_id
     *
     * @param depId company_deps.dep_id, id
     */
    public void setDepId(Long depId) {
        this.depId = depId;
    }

    /**
     * 获取 所属企业id 字段:company_deps.dep_company_id
     *
     * @return company_deps.dep_company_id, 所属企业id
     */
    public Long getDepCompanyId() {
        return depCompanyId;
    }

    /**
     * 设置 所属企业id 字段:company_deps.dep_company_id
     *
     * @param depCompanyId company_deps.dep_company_id, 所属企业id
     */
    public void setDepCompanyId(Long depCompanyId) {
        this.depCompanyId = depCompanyId;
    }

    /**
     * 获取 上级id 字段:company_deps.dep_parent_id
     *
     * @return company_deps.dep_parent_id, 上级id
     */
    public Long getDepParentId() {
        return depParentId;
    }

    /**
     * 设置 上级id 字段:company_deps.dep_parent_id
     *
     * @param depParentId company_deps.dep_parent_id, 上级id
     */
    public void setDepParentId(Long depParentId) {
        this.depParentId = depParentId;
    }

    /**
     * 获取 本门名称 字段:company_deps.dep_name
     *
     * @return company_deps.dep_name, 本门名称
     */
    public String getDepName() {
        return depName;
    }

    /**
     * 设置 本门名称 字段:company_deps.dep_name
     *
     * @param depName company_deps.dep_name, 本门名称
     */
    public void setDepName(String depName) {
        this.depName = depName == null ? null : depName.trim();
    }

    /**
     * 获取 部门编号 字段:company_deps.dep_number
     *
     * @return company_deps.dep_number, 部门编号
     */
    public String getDepNumber() {
        return depNumber;
    }

    /**
     * 设置 部门编号 字段:company_deps.dep_number
     *
     * @param depNumber company_deps.dep_number, 部门编号
     */
    public void setDepNumber(String depNumber) {
        this.depNumber = depNumber == null ? null : depNumber.trim();
    }

    /**
     * 获取 1总公司(集团) 2部门 3子公司 字段:company_deps.dep_type
     *
     * @return company_deps.dep_type, 1总公司(集团) 2部门 3子公司
     */
    public Integer getDepType() {
        return depType;
    }

    /**
     * 设置 1总公司(集团) 2部门 3子公司 字段:company_deps.dep_type
     *
     * @param depType company_deps.dep_type, 1总公司(集团) 2部门 3子公司
     */
    public void setDepType(Integer depType) {
        this.depType = depType;
    }

    /**
     * 获取 发薪日 0未设置 字段:company_deps.dep_salary_day
     *
     * @return company_deps.dep_salary_day, 发薪日 0未设置
     */
    public Integer getDepSalaryDay() {
        return depSalaryDay;
    }

    /**
     * 设置 发薪日 0未设置 字段:company_deps.dep_salary_day
     *
     * @param depSalaryDay company_deps.dep_salary_day, 发薪日 0未设置
     */
    public void setDepSalaryDay(Integer depSalaryDay) {
        this.depSalaryDay = depSalaryDay;
    }

    /**
     * 获取 状态 0禁用 1可用 字段:company_deps.dep_sign
     *
     * @return company_deps.dep_sign, 状态 0禁用 1可用
     */
    public Integer getDepSign() {
        return depSign;
    }

    /**
     * 设置 状态 0禁用 1可用 字段:company_deps.dep_sign
     *
     * @param depSign company_deps.dep_sign, 状态 0禁用 1可用
     */
    public void setDepSign(Integer depSign) {
        this.depSign = depSign;
    }

    /**
     * 获取 薪资计算周期 字段:company_deps.dep_salary_sumdays
     *
     * @return company_deps.dep_salary_sumdays, 薪资计算周期
     */
    public Integer getDepSalarySumdays() {
        return depSalarySumdays;
    }

    /**
     * 设置 薪资计算周期 字段:company_deps.dep_salary_sumdays
     *
     * @param depSalarySumdays company_deps.dep_salary_sumdays, 薪资计算周期
     */
    public void setDepSalarySumdays(Integer depSalarySumdays) {
        this.depSalarySumdays = depSalarySumdays;
    }

    /**
     * 获取 员工上传日 字段:company_deps.dep_updateuser_date
     *
     * @return company_deps.dep_updateuser_date, 员工上传日
     */
    public Integer getDepUpdateuserDate() {
        return depUpdateuserDate;
    }

    /**
     * 设置 员工上传日 字段:company_deps.dep_updateuser_date
     *
     * @param depUpdateuserDate company_deps.dep_updateuser_date, 员工上传日
     */
    public void setDepUpdateuserDate(Integer depUpdateuserDate) {
        this.depUpdateuserDate = depUpdateuserDate;
    }

    /**
     * 获取 修改人 字段:company_deps.dep_edit_member
     *
     * @return company_deps.dep_edit_member, 修改人
     */
    public Long getDepEditMember() {
        return depEditMember;
    }

    /**
     * 设置 修改人 字段:company_deps.dep_edit_member
     *
     * @param depEditMember company_deps.dep_edit_member, 修改人
     */
    public void setDepEditMember(Long depEditMember) {
        this.depEditMember = depEditMember;
    }

    /**
     * 获取 修改时间 字段:company_deps.dep_edit_time
     *
     * @return company_deps.dep_edit_time, 修改时间
     */
    public Date getDepEditTime() {
        return depEditTime;
    }

    /**
     * 设置 修改时间 字段:company_deps.dep_edit_time
     *
     * @param depEditTime company_deps.dep_edit_time, 修改时间
     */
    public void setDepEditTime(Date depEditTime) {
        this.depEditTime = depEditTime;
    }

    /**
     * 获取 企业id 字段:company_deps.company_id
     *
     * @return company_deps.company_id, 企业id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 企业id 字段:company_deps.company_id
     *
     * @param companyId company_deps.company_id, 企业id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 所属层级 1 开始 字段:company_deps.dep_level
     *
     * @return company_deps.dep_level, 所属层级 1 开始
     */
    public Integer getDepLevel() {
        return depLevel;
    }

    /**
     * 设置 所属层级 1 开始 字段:company_deps.dep_level
     *
     * @param depLevel company_deps.dep_level, 所属层级 1 开始
     */
    public void setDepLevel(Integer depLevel) {
        this.depLevel = depLevel;
    }

    public Integer getDepCustCount() {
        return depCustCount;
    }

    public void setDepCustCount(Integer depCustCount) {
        this.depCustCount = depCustCount;
    }

    public Long getDepLeader() {
        return depLeader;
    }

    public void setDepLeader(Long depLeader) {
        this.depLeader = depLeader;
    }

    public List<CompanyDepsBean> getChildren() {
        return children;
    }

    public void setChildren(List<CompanyDepsBean> children) {
        this.children = children;
    }

    public Integer getDelflag() {
        return delflag;
    }

    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}