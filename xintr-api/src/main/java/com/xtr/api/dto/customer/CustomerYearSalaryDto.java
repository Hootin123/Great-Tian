package com.xtr.api.dto.customer;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 员工本年度的工资
 * @author:zhangshuai
 * @date: 2016/9/8.
 */
public class CustomerYearSalaryDto implements Serializable{

     /***公司记薪周期表id**/
     private Long payCycleId;

     /**公司id**/
     private Long companyId;
     /**年份**/
     private String year;
     /**月**/
     private String month;

     /**员工工资单id**/
     private Long customerPayrollId;

     /**基本工资**/
     private BigDecimal baseWages;

     /**实发工资**/
     private BigDecimal realWage;

    public BigDecimal getRealWage() {
        return realWage;
    }

    public void setRealWage(BigDecimal realWage) {
        this.realWage = realWage;
    }

    public Long getPayCycleId() {
        return payCycleId;
    }

    public void setPayCycleId(Long payCycleId) {
        this.payCycleId = payCycleId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public Long getCustomerPayrollId() {
        return customerPayrollId;
    }

    public void setCustomerPayrollId(Long customerPayrollId) {
        this.customerPayrollId = customerPayrollId;
    }

    public BigDecimal getBaseWages() {
        return baseWages;
    }

    public void setBaseWages(BigDecimal baseWages) {
        this.baseWages = baseWages;
    }
}
