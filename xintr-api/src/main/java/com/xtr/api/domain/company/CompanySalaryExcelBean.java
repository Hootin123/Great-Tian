package com.xtr.api.domain.company;

import com.weibo.api.motan.core.extension.SpiMeta;
import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 企业上传工资文档
 */
public class CompanySalaryExcelBean extends BaseObject implements Serializable {

    /**
     * id,所属表字段为company_salary_excel.excel_id
     */
    private Long excelId;

    /**
     * 企业id,所属表字段为company_salary_excel.excel_company_id
     */
    private Long excelCompanyId;

    /**
     * 子公司id,所属表字段为company_salary_excel.excel_dep_id
     */
    private Long excelDepId;

    /**
     * 年,所属表字段为company_salary_excel.excel_year
     */
    private Integer excelYear;

    /**
     * 月,所属表字段为company_salary_excel.excel_month
     */
    private Integer excelMonth;

    /**
     * 文档名称,所属表字段为company_salary_excel.excel_name
     */
    private String excelName;

    /**
     * 创建时间,所属表字段为company_salary_excel.excel_addtime
     */
    private Date excelAddtime;

    /**
     * 0待审核 1初审完成 2复审完成 3审批失败 4审批成功 5工资已发 备注:(目前不清楚是以上传文档为单位还是以个人为单位审核),所属表字段为company_salary_excel.excel_state
     */
    private Integer excelState;

    /**
     * 路径,所属表字段为company_salary_excel.excel_path
     */
    private String excelPath;

    /**
     * 添加人,所属表字段为company_salary_excel.excel_addmember
     */
    private Long excelAddmember;

    /**
     * 文件实发工资总额,所属表字段为company_salary_excel.excel_salary_total
     */
    private BigDecimal excelSalaryTotal;

    /**
     * 总成本,所属表字段为company_salary_excel.excel_cost_total
     */
    private BigDecimal excelCostTotal;

    /**
     * 初审时间 逗号隔开 ,123,123...................,,所属表字段为company_salary_excel.excel_audit_time_first
     */
    private String excelAuditTimeFirst;

    /**
     * 初审人 ,aa,bb,cc.......,,所属表字段为company_salary_excel.excel_audit_member_first
     */
    private String excelAuditMemberFirst;

    /**
     * ,所属表字段为company_salary_excel.excel_audit_remark_first
     */
    private String excelAuditRemarkFirst;

    /**
     * 复审时间,所属表字段为company_salary_excel.excel_audit_time_second
     */
    private String excelAuditTimeSecond;

    /**
     * 复审人 ,aa,bb,cc.................,,所属表字段为company_salary_excel.excel_audit_member_second
     */
    private String excelAuditMemberSecond;

    /**
     * ,所属表字段为company_salary_excel.excel_audit_remark_second
     */
    private String excelAuditRemarkSecond;

    /**
     * 审批时间,所属表字段为company_salary_excel.excel_audit_time_third
     */
    private String excelAuditTimeThird;

    /**
     * 审批人 ,aa,bb,cc...........,,所属表字段为company_salary_excel.excel_audit_member_third
     */
    private String excelAuditMemberThird;

    /**
     * 审批备注,所属表字段为company_salary_excel.excel_audit_remark_third
     */
    private String excelAuditRemarkThird;

    /**
     * 工资发放状态 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
     */
    private Integer excelGrantState;

    /**
     * 工资发放日期
     */
    private Date excelPayday;

    /**
     * 添加人姓名
     */
    private String excelAddName;

    /**
     * 发薪人数
     */
    private Integer excelPayrollNumber;

    /**
     * 文件组名
     */
    private String excelGroupName;

    /**
     * 获取 id 字段:company_salary_excel.excel_id
     *
     * @return company_salary_excel.excel_id, id
     */
    public Long getExcelId() {
        return excelId;
    }

    /**
     * 设置 id 字段:company_salary_excel.excel_id
     *
     * @param excelId company_salary_excel.excel_id, id
     */
    public void setExcelId(Long excelId) {
        this.excelId = excelId;
    }

    /**
     * 获取 企业id 字段:company_salary_excel.excel_company_id
     *
     * @return company_salary_excel.excel_company_id, 企业id
     */
    public Long getExcelCompanyId() {
        return excelCompanyId;
    }

    /**
     * 设置 企业id 字段:company_salary_excel.excel_company_id
     *
     * @param excelCompanyId company_salary_excel.excel_company_id, 企业id
     */
    public void setExcelCompanyId(Long excelCompanyId) {
        this.excelCompanyId = excelCompanyId;
    }

    /**
     * 获取 子公司id 字段:company_salary_excel.excel_dep_id
     *
     * @return company_salary_excel.excel_dep_id, 子公司id
     */
    public Long getExcelDepId() {
        return excelDepId;
    }

    /**
     * 设置 子公司id 字段:company_salary_excel.excel_dep_id
     *
     * @param excelDepId company_salary_excel.excel_dep_id, 子公司id
     */
    public void setExcelDepId(Long excelDepId) {
        this.excelDepId = excelDepId;
    }

    /**
     * 获取 年 字段:company_salary_excel.excel_year
     *
     * @return company_salary_excel.excel_year, 年
     */
    public Integer getExcelYear() {
        return excelYear;
    }

    /**
     * 设置 年 字段:company_salary_excel.excel_year
     *
     * @param excelYear company_salary_excel.excel_year, 年
     */
    public void setExcelYear(Integer excelYear) {
        this.excelYear = excelYear;
    }

    /**
     * 获取 月 字段:company_salary_excel.excel_month
     *
     * @return company_salary_excel.excel_month, 月
     */
    public Integer getExcelMonth() {
        return excelMonth;
    }

    /**
     * 设置 月 字段:company_salary_excel.excel_month
     *
     * @param excelMonth company_salary_excel.excel_month, 月
     */
    public void setExcelMonth(Integer excelMonth) {
        this.excelMonth = excelMonth;
    }

    /**
     * 获取 文档名称 字段:company_salary_excel.excel_name
     *
     * @return company_salary_excel.excel_name, 文档名称
     */
    public String getExcelName() {
        return excelName;
    }

    /**
     * 设置 文档名称 字段:company_salary_excel.excel_name
     *
     * @param excelName company_salary_excel.excel_name, 文档名称
     */
    public void setExcelName(String excelName) {
        this.excelName = excelName == null ? null : excelName.trim();
    }

    /**
     * 获取 创建时间 字段:company_salary_excel.excel_addtime
     *
     * @return company_salary_excel.excel_addtime, 创建时间
     */
    public Date getExcelAddtime() {
        return excelAddtime;
    }

    /**
     * 设置 创建时间 字段:company_salary_excel.excel_addtime
     *
     * @param excelAddtime company_salary_excel.excel_addtime, 创建时间
     */
    public void setExcelAddtime(Date excelAddtime) {
        this.excelAddtime = excelAddtime;
    }

    /**
     * 获取 0待审核 1初审完成 2复审完成 3审批失败 4审批成功 5工资已发 备注:(目前不清楚是以上传文档为单位还是以个人为单位审核) 字段:company_salary_excel.excel_state
     *
     * @return company_salary_excel.excel_state, 0待审核 1初审完成 2复审完成 3审批失败 4审批成功 5工资已发 备注:(目前不清楚是以上传文档为单位还是以个人为单位审核)
     */
    public Integer getExcelState() {
        return excelState;
    }

    /**
     * 设置 0待审核 1初审完成 2复审完成 3审批失败 4审批成功 5工资已发 备注:(目前不清楚是以上传文档为单位还是以个人为单位审核) 字段:company_salary_excel.excel_state
     *
     * @param excelState company_salary_excel.excel_state, 0待审核 1初审完成 2复审完成 3审批失败 4审批成功 5工资已发 备注:(目前不清楚是以上传文档为单位还是以个人为单位审核)
     */
    public void setExcelState(Integer excelState) {
        this.excelState = excelState;
    }

    /**
     * 获取 路径 字段:company_salary_excel.excel_path
     *
     * @return company_salary_excel.excel_path, 路径
     */
    public String getExcelPath() {
        return excelPath;
    }

    /**
     * 设置 路径 字段:company_salary_excel.excel_path
     *
     * @param excelPath company_salary_excel.excel_path, 路径
     */
    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath == null ? null : excelPath.trim();
    }

    /**
     * 获取 添加人 字段:company_salary_excel.excel_addmember
     *
     * @return company_salary_excel.excel_addmember, 添加人
     */
    public Long getExcelAddmember() {
        return excelAddmember;
    }

    /**
     * 设置 添加人 字段:company_salary_excel.excel_addmember
     *
     * @param excelAddmember company_salary_excel.excel_addmember, 添加人
     */
    public void setExcelAddmember(Long excelAddmember) {
        this.excelAddmember = excelAddmember;
    }

    /**
     * 获取 文件实发工资总额 字段:company_salary_excel.excel_salary_total
     *
     * @return company_salary_excel.excel_salary_total, 文件实发工资总额
     */
    public BigDecimal getExcelSalaryTotal() {
        return excelSalaryTotal;
    }

    /**
     * 设置 文件实发工资总额 字段:company_salary_excel.excel_salary_total
     *
     * @param excelSalaryTotal company_salary_excel.excel_salary_total, 文件实发工资总额
     */
    public void setExcelSalaryTotal(BigDecimal excelSalaryTotal) {
        this.excelSalaryTotal = excelSalaryTotal;
    }

    /**
     * 获取 总成本 字段:company_salary_excel.excel_cost_total
     *
     * @return company_salary_excel.excel_cost_total, 总成本
     */
    public BigDecimal getExcelCostTotal() {
        return excelCostTotal;
    }

    /**
     * 设置 总成本 字段:company_salary_excel.excel_cost_total
     *
     * @param excelCostTotal company_salary_excel.excel_cost_total, 总成本
     */
    public void setExcelCostTotal(BigDecimal excelCostTotal) {
        this.excelCostTotal = excelCostTotal;
    }

    /**
     * 获取 初审时间 逗号隔开 ,123,123..................., 字段:company_salary_excel.excel_audit_time_first
     *
     * @return company_salary_excel.excel_audit_time_first, 初审时间 逗号隔开 ,123,123...................,
     */
    public String getExcelAuditTimeFirst() {
        return excelAuditTimeFirst;
    }

    /**
     * 设置 初审时间 逗号隔开 ,123,123..................., 字段:company_salary_excel.excel_audit_time_first
     *
     * @param excelAuditTimeFirst company_salary_excel.excel_audit_time_first, 初审时间 逗号隔开 ,123,123...................,
     */
    public void setExcelAuditTimeFirst(String excelAuditTimeFirst) {
        this.excelAuditTimeFirst = excelAuditTimeFirst == null ? null : excelAuditTimeFirst.trim();
    }

    /**
     * 获取 初审人 ,aa,bb,cc......., 字段:company_salary_excel.excel_audit_member_first
     *
     * @return company_salary_excel.excel_audit_member_first, 初审人 ,aa,bb,cc.......,
     */
    public String getExcelAuditMemberFirst() {
        return excelAuditMemberFirst;
    }

    /**
     * 设置 初审人 ,aa,bb,cc......., 字段:company_salary_excel.excel_audit_member_first
     *
     * @param excelAuditMemberFirst company_salary_excel.excel_audit_member_first, 初审人 ,aa,bb,cc.......,
     */
    public void setExcelAuditMemberFirst(String excelAuditMemberFirst) {
        this.excelAuditMemberFirst = excelAuditMemberFirst == null ? null : excelAuditMemberFirst.trim();
    }

    /**
     * 获取  字段:company_salary_excel.excel_audit_remark_first
     *
     * @return company_salary_excel.excel_audit_remark_first,
     */
    public String getExcelAuditRemarkFirst() {
        return excelAuditRemarkFirst;
    }

    /**
     * 设置  字段:company_salary_excel.excel_audit_remark_first
     *
     * @param excelAuditRemarkFirst company_salary_excel.excel_audit_remark_first,
     */
    public void setExcelAuditRemarkFirst(String excelAuditRemarkFirst) {
        this.excelAuditRemarkFirst = excelAuditRemarkFirst == null ? null : excelAuditRemarkFirst.trim();
    }

    /**
     * 获取 复审时间 字段:company_salary_excel.excel_audit_time_second
     *
     * @return company_salary_excel.excel_audit_time_second, 复审时间
     */
    public String getExcelAuditTimeSecond() {
        return excelAuditTimeSecond;
    }

    /**
     * 设置 复审时间 字段:company_salary_excel.excel_audit_time_second
     *
     * @param excelAuditTimeSecond company_salary_excel.excel_audit_time_second, 复审时间
     */
    public void setExcelAuditTimeSecond(String excelAuditTimeSecond) {
        this.excelAuditTimeSecond = excelAuditTimeSecond == null ? null : excelAuditTimeSecond.trim();
    }

    /**
     * 获取 复审人 ,aa,bb,cc................., 字段:company_salary_excel.excel_audit_member_second
     *
     * @return company_salary_excel.excel_audit_member_second, 复审人 ,aa,bb,cc.................,
     */
    public String getExcelAuditMemberSecond() {
        return excelAuditMemberSecond;
    }

    /**
     * 设置 复审人 ,aa,bb,cc................., 字段:company_salary_excel.excel_audit_member_second
     *
     * @param excelAuditMemberSecond company_salary_excel.excel_audit_member_second, 复审人 ,aa,bb,cc.................,
     */
    public void setExcelAuditMemberSecond(String excelAuditMemberSecond) {
        this.excelAuditMemberSecond = excelAuditMemberSecond == null ? null : excelAuditMemberSecond.trim();
    }

    /**
     * 获取  字段:company_salary_excel.excel_audit_remark_second
     *
     * @return company_salary_excel.excel_audit_remark_second,
     */
    public String getExcelAuditRemarkSecond() {
        return excelAuditRemarkSecond;
    }

    /**
     * 设置  字段:company_salary_excel.excel_audit_remark_second
     *
     * @param excelAuditRemarkSecond company_salary_excel.excel_audit_remark_second,
     */
    public void setExcelAuditRemarkSecond(String excelAuditRemarkSecond) {
        this.excelAuditRemarkSecond = excelAuditRemarkSecond == null ? null : excelAuditRemarkSecond.trim();
    }

    /**
     * 获取 审批时间 字段:company_salary_excel.excel_audit_time_third
     *
     * @return company_salary_excel.excel_audit_time_third, 审批时间
     */
    public String getExcelAuditTimeThird() {
        return excelAuditTimeThird;
    }

    /**
     * 设置 审批时间 字段:company_salary_excel.excel_audit_time_third
     *
     * @param excelAuditTimeThird company_salary_excel.excel_audit_time_third, 审批时间
     */
    public void setExcelAuditTimeThird(String excelAuditTimeThird) {
        this.excelAuditTimeThird = excelAuditTimeThird == null ? null : excelAuditTimeThird.trim();
    }

    /**
     * 获取 审批人 ,aa,bb,cc..........., 字段:company_salary_excel.excel_audit_member_third
     *
     * @return company_salary_excel.excel_audit_member_third, 审批人 ,aa,bb,cc...........,
     */
    public String getExcelAuditMemberThird() {
        return excelAuditMemberThird;
    }

    /**
     * 设置 审批人 ,aa,bb,cc..........., 字段:company_salary_excel.excel_audit_member_third
     *
     * @param excelAuditMemberThird company_salary_excel.excel_audit_member_third, 审批人 ,aa,bb,cc...........,
     */
    public void setExcelAuditMemberThird(String excelAuditMemberThird) {
        this.excelAuditMemberThird = excelAuditMemberThird == null ? null : excelAuditMemberThird.trim();
    }

    /**
     * 获取 审批备注 字段:company_salary_excel.excel_audit_remark_third
     *
     * @return company_salary_excel.excel_audit_remark_third, 审批备注
     */
    public String getExcelAuditRemarkThird() {
        return excelAuditRemarkThird;
    }

    /**
     * 设置 审批备注 字段:company_salary_excel.excel_audit_remark_third
     *
     * @param excelAuditRemarkThird company_salary_excel.excel_audit_remark_third, 审批备注
     */
    public void setExcelAuditRemarkThird(String excelAuditRemarkThird) {
        this.excelAuditRemarkThird = excelAuditRemarkThird == null ? null : excelAuditRemarkThird.trim();
    }

    public Integer getExcelGrantState() {
        return excelGrantState;
    }

    public void setExcelGrantState(Integer excelGrantState) {
        this.excelGrantState = excelGrantState;
    }

    public Date getExcelPayday() {
        return excelPayday;
    }

    public void setExcelPayday(Date excelPayday) {
        this.excelPayday = excelPayday;
    }

    public String getExcelAddName() {
        return excelAddName;
    }

    public void setExcelAddName(String excelAddName) {
        this.excelAddName = excelAddName;
    }

    public Integer getExcelPayrollNumber() {
        return excelPayrollNumber;
    }

    public void setExcelPayrollNumber(Integer excelPayrollNumber) {
        this.excelPayrollNumber = excelPayrollNumber;
    }

    public String getExcelGroupName() {
        return excelGroupName;
    }

    public void setExcelGroupName(String excelGroupName) {
        this.excelGroupName = excelGroupName;
    }
}