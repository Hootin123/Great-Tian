package com.xtr.api.dto.salary;

import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.CustomerPayrollDetailBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>工资核算</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/17 10:37
 */
public class CustomerPayrollDto extends CustomerPayrollBean implements Serializable {

    /**
     * 姓名
     */
    private String userName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 职位
     */
    private String stationName;

    /**
     * 入职日期
     */
    private Date entryDate;

    /**
     * 聘用形式 1正式 2劳务
     */
    private Integer employMethod;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 工资卡卡号
     */
    private String bankNumber;

    /**
     * 开户银行
     */
    private String bankAccount;

    /**
     * 是否定薪 0:未定  1:已定
     */
    private String isSalary;

    /**
     * 员工状态 1入职 2转正 3离职 4删除
     */
    private Integer stationCustomerState;

    /**
     * 津贴列表项
     */
    private List<CustomerPayrollDetailBean> allowanceList;

    /**
     * 奖金列表项
     */
    private List<CustomerPayrollDetailBean> bonusList;

    /**
     * 人员信息
     */
    private List customerList;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 报销额度
     */
    private BigDecimal customerCurrentExpense;

    public List<CustomerPayrollDetailBean> getAllowanceList() {
        return allowanceList;
    }

    public void setAllowanceList(List<CustomerPayrollDetailBean> allowanceList) {
        this.allowanceList = allowanceList;
    }

    public List<CustomerPayrollDetailBean> getBonusList() {
        return bonusList;
    }

    public void setBonusList(List<CustomerPayrollDetailBean> bonusList) {
        this.bonusList = bonusList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getEmployMethod() {
        return employMethod;
    }

    public void setEmployMethod(Integer employMethod) {
        this.employMethod = employMethod;
    }

    public String getIsSalary() {
        return isSalary;
    }

    public void setIsSalary(String isSalary) {
        this.isSalary = isSalary;
    }

    public Integer getStationCustomerState() {
        return stationCustomerState;
    }

    public void setStationCustomerState(Integer stationCustomerState) {
        this.stationCustomerState = stationCustomerState;
    }

    public List getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List customerList) {
        this.customerList = customerList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCustomerCurrentExpense() {
        return customerCurrentExpense;
    }

    public void setCustomerCurrentExpense(BigDecimal customerCurrentExpense) {
        this.customerCurrentExpense = customerCurrentExpense;
    }
}
