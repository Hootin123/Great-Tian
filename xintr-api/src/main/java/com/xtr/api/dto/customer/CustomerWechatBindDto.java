package com.xtr.api.dto.customer;

import com.xtr.api.domain.customer.CustomersBean;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工微信绑定查询实体
 * @author:zhangshuai
 * @date: 2016/9/8.
 */
public class CustomerWechatBindDto extends CustomersBean implements Serializable{

    /**年度**/
    private  String  year;
    /**月份**/
    private String month;

    /**发薪日期**/
    private Date  payDay;

    /**计薪周期的id**/
    private Long payCycleId;
    /**工资单id**/
    private Long  payrollId;
    /**发薪日期的string格式**/
    private String payDayStr;

    public String getPayDayStr() {
        return payDayStr;
    }

    public void setPayDayStr(String payDayStr) {
        this.payDayStr = payDayStr;
    }

    public Long getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Long payrollId) {
        this.payrollId = payrollId;
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

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public Long getPayCycleId() {
        return payCycleId;
    }

    public void setPayCycleId(Long payCycleId) {
        this.payCycleId = payCycleId;
    }
}
