package com.xtr.api.domain.salary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RapidlyPayrollExcelBean implements Serializable {
    /**
     * 主键,所属表字段为rapidly_payroll_excel.id
     */
    private Long id;

    /**
     * 公司主键,所属表字段为rapidly_payroll_excel.company_id
     */
    private Long companyId;

    /**
     * 文档名称,所属表字段为rapidly_payroll_excel.excel_name
     */
    private String excelName;

    /**
     * 文档标题,所属表字段为rapidly_payroll_excel.excel_title
     */
    private String excelTitle;

    /**
     * excel路径,所属表字段为rapidly_payroll_excel.excel_path
     */
    private String excelPath;

    /**
     * 文件组名称,所属表字段为rapidly_payroll_excel.excel_group_name
     */
    private String excelGroupName;

    /**
     * 工资总额,所属表字段为rapidly_payroll_excel.total_wages
     */
    private BigDecimal totalWages;

    /**
     * 发薪人数,所属表字段为rapidly_payroll_excel.payroll_number
     */
    private Integer payrollNumber;

    /**
     * 发薪订单号,所属表字段为rapidly_payroll_excel.salary_order
     */
    private String salaryOrder;

    /**
     * 0:未发放 1:待发放  2:发放中  3:已发放,所属表字段为rapidly_payroll_excel.grant_state
     */
    private Integer grantState;

    /**
     * 创建人,所属表字段为rapidly_payroll_excel.create_user
     */
    private Long createUser;

    /**
     * 创建时间,所属表字段为rapidly_payroll_excel.create_time
     */
    private Date createTime;

    /**
     * 备注,所属表字段为rapidly_payroll_excel.remark
     */
    private String remark;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 发薪日期
     */
    private Date payDay;

    /**
     * 是否生成工资单 0:未生成 1:已生成
     */
    private Integer isGeneratePayroll;

    /**
     * 工资明细
     */
    private List<RapidlyPayrollBean> payrollList;

    /**
     * 获取 主键 字段:rapidly_payroll_excel.id
     *
     * @return rapidly_payroll_excel.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:rapidly_payroll_excel.id
     *
     * @param id rapidly_payroll_excel.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 公司主键 字段:rapidly_payroll_excel.company_id
     *
     * @return rapidly_payroll_excel.company_id, 公司主键
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 公司主键 字段:rapidly_payroll_excel.company_id
     *
     * @param companyId rapidly_payroll_excel.company_id, 公司主键
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 文档名称 字段:rapidly_payroll_excel.excel_name
     *
     * @return rapidly_payroll_excel.excel_name, 文档名称
     */
    public String getExcelName() {
        return excelName;
    }

    /**
     * 设置 文档名称 字段:rapidly_payroll_excel.excel_name
     *
     * @param excelName rapidly_payroll_excel.excel_name, 文档名称
     */
    public void setExcelName(String excelName) {
        this.excelName = excelName == null ? null : excelName.trim();
    }

    /**
     * 获取 excel路径 字段:rapidly_payroll_excel.excel_path
     *
     * @return rapidly_payroll_excel.excel_path, excel路径
     */
    public String getExcelPath() {
        return excelPath;
    }

    /**
     * 设置 excel路径 字段:rapidly_payroll_excel.excel_path
     *
     * @param excelPath rapidly_payroll_excel.excel_path, excel路径
     */
    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath == null ? null : excelPath.trim();
    }

    /**
     * 获取 文件组名称 字段:rapidly_payroll_excel.excel_group_name
     *
     * @return rapidly_payroll_excel.excel_group_name, 文件组名称
     */
    public String getExcelGroupName() {
        return excelGroupName;
    }

    /**
     * 设置 文件组名称 字段:rapidly_payroll_excel.excel_group_name
     *
     * @param excelGroupName rapidly_payroll_excel.excel_group_name, 文件组名称
     */
    public void setExcelGroupName(String excelGroupName) {
        this.excelGroupName = excelGroupName == null ? null : excelGroupName.trim();
    }

    /**
     * 获取 工资总额 字段:rapidly_payroll_excel.total_wages
     *
     * @return rapidly_payroll_excel.total_wages, 工资总额
     */
    public BigDecimal getTotalWages() {
        return totalWages;
    }

    /**
     * 设置 工资总额 字段:rapidly_payroll_excel.total_wages
     *
     * @param totalWages rapidly_payroll_excel.total_wages, 工资总额
     */
    public void setTotalWages(BigDecimal totalWages) {
        this.totalWages = totalWages;
    }

    /**
     * 获取 发薪人数 字段:rapidly_payroll_excel.payroll_number
     *
     * @return rapidly_payroll_excel.payroll_number, 发薪人数
     */
    public Integer getPayrollNumber() {
        return payrollNumber;
    }

    /**
     * 设置 发薪人数 字段:rapidly_payroll_excel.payroll_number
     *
     * @param payrollNumber rapidly_payroll_excel.payroll_number, 发薪人数
     */
    public void setPayrollNumber(Integer payrollNumber) {
        this.payrollNumber = payrollNumber;
    }

    /**
     * 获取 发薪订单号 字段:rapidly_payroll_excel.salary_order
     *
     * @return rapidly_payroll_excel.salary_order, 发薪订单号
     */
    public String getSalaryOrder() {
        return salaryOrder;
    }

    /**
     * 设置 发薪订单号 字段:rapidly_payroll_excel.salary_order
     *
     * @param salaryOrder rapidly_payroll_excel.salary_order, 发薪订单号
     */
    public void setSalaryOrder(String salaryOrder) {
        this.salaryOrder = salaryOrder == null ? null : salaryOrder.trim();
    }

    /**
     * 获取 0:未发放 1:待发放  2:发放中  3:已发放 字段:rapidly_payroll_excel.grant_state
     *
     * @return rapidly_payroll_excel.grant_state, 0:未发放 1:待发放  2:发放中  3:已发放
     */
    public Integer getGrantState() {
        return grantState;
    }

    /**
     * 设置 0:未发放 1:待发放  2:发放中  3:已发放 字段:rapidly_payroll_excel.grant_state
     *
     * @param grantState rapidly_payroll_excel.grant_state, 0:未发放 1:待发放  2:发放中  3:已发放
     */
    public void setGrantState(Integer grantState) {
        this.grantState = grantState;
    }

    /**
     * 获取 创建人 字段:rapidly_payroll_excel.create_user
     *
     * @return rapidly_payroll_excel.create_user, 创建人
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 设置 创建人 字段:rapidly_payroll_excel.create_user
     *
     * @param createUser rapidly_payroll_excel.create_user, 创建人
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取 创建时间 字段:rapidly_payroll_excel.create_time
     *
     * @return rapidly_payroll_excel.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:rapidly_payroll_excel.create_time
     *
     * @param createTime rapidly_payroll_excel.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 备注 字段:rapidly_payroll_excel.remark
     *
     * @return rapidly_payroll_excel.remark, 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 字段:rapidly_payroll_excel.remark
     *
     * @param remark rapidly_payroll_excel.remark, 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public List<RapidlyPayrollBean> getPayrollList() {
        return payrollList;
    }

    public void setPayrollList(List<RapidlyPayrollBean> payrollList) {
        this.payrollList = payrollList;
    }

    public String getExcelTitle() {
        return excelTitle;
    }

    public void setExcelTitle(String excelTitle) {
        this.excelTitle = excelTitle;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public Integer getIsGeneratePayroll() {
        return isGeneratePayroll;
    }

    public void setIsGeneratePayroll(Integer isGeneratePayroll) {
        this.isGeneratePayroll = isGeneratePayroll;
    }
}