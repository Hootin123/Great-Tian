package com.xtr.api.dto.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工资详情查询类
 * @author:zhangshuai
 * @date: 2016/9/9.
 */
public class CustomerPayrollWechatBindDto implements Serializable {



    //公司名称
    private String  companyName;
    //部门名称
    private String customerDepName;
    //姓名
    private String customerTurename;
   //员工工号
    private String customerNumber;
   //银行卡号
    private String customerBanknumber;
   //实发工资
    private BigDecimal realWage;
   //发薪日期
    private Date payDay;
    //基本工资
    private BigDecimal baseWages;
    //缺勤天数
    private BigDecimal absenceDayNumber;
    //考勤扣款
    private BigDecimal attendanceDeduction;
    //奖金
    private BigDecimal totalBonus;
    //税前补发/扣款
    private BigDecimal pretax;
    //社保公积金
    private BigDecimal socialSecurityFund;
    //应发工资
    private BigDecimal shouldAmount;
    //个税
    private BigDecimal personalTax;
    //税后
     private BigDecimal afterTax;

    //月份
    private String month;
    //津贴名称
    private String detailName;
    //津贴金额
    private BigDecimal detailValue;

    //当前月如：（6.1-7.1）
    private String currentMonthStr;

    //报销金额
    private  BigDecimal wipedAmount;

    //绩效补充
    private BigDecimal  appraisalsSupplement;

    //社保
    private BigDecimal socialSecurity;
    //公积金
    private BigDecimal fund;

    private  BigDecimal addWages;

    public BigDecimal getAddWages() {
        return addWages;
    }

    public void setAddWages(BigDecimal addWages) {
        this.addWages = addWages;
    }

    public BigDecimal getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(BigDecimal socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
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

    public String getCurrentMonthStr() {
        return currentMonthStr;
    }

    public void setCurrentMonthStr(String currentMonthStr) {
        this.currentMonthStr = currentMonthStr;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public BigDecimal getDetailValue() {
        return detailValue;
    }

    public void setDetailValue(BigDecimal detailValue) {
        this.detailValue = detailValue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        month = month;
    }

    public String getCustomerDepName() {
        return customerDepName;
    }

    public void setCustomerDepName(String customerDepName) {
        this.customerDepName = customerDepName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerTurename() {
        return customerTurename;
    }

    public void setCustomerTurename(String customerTurename) {
        this.customerTurename = customerTurename;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerBanknumber() {
        return customerBanknumber;
    }

    public void setCustomerBanknumber(String customerBanknumber) {
        this.customerBanknumber = customerBanknumber;
    }

    public BigDecimal getRealWage() {
        return realWage;
    }

    public void setRealWage(BigDecimal realWage) {
        this.realWage = realWage;
    }

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public BigDecimal getBaseWages() {
        return baseWages;
    }

    public void setBaseWages(BigDecimal baseWages) {
        this.baseWages = baseWages;
    }

    public BigDecimal getAttendanceDeduction() {
        return attendanceDeduction;
    }

    public void setAttendanceDeduction(BigDecimal attendanceDeduction) {
        this.attendanceDeduction = attendanceDeduction;
    }

    public BigDecimal getSocialSecurityFund() {
        return socialSecurityFund;
    }

    public void setSocialSecurityFund(BigDecimal socialSecurityFund) {
        this.socialSecurityFund = socialSecurityFund;
    }

    public BigDecimal getShouldAmount() {
        return shouldAmount;
    }

    public void setShouldAmount(BigDecimal shouldAmount) {
        this.shouldAmount = shouldAmount;
    }

    public BigDecimal getAfterTax() {
        return afterTax;
    }

    public void setAfterTax(BigDecimal afterTax) {
        this.afterTax = afterTax;
    }

    public BigDecimal getPersonalTax() {
        return personalTax;
    }

    public void setPersonalTax(BigDecimal personalTax) {
        this.personalTax = personalTax;
    }

    public BigDecimal getPretax() {
        return pretax;
    }

    public void setPretax(BigDecimal pretax) {
        this.pretax = pretax;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public BigDecimal getAbsenceDayNumber() {
        return absenceDayNumber;
    }

    public void setAbsenceDayNumber(BigDecimal absenceDayNumber) {
        this.absenceDayNumber = absenceDayNumber;
    }
}
