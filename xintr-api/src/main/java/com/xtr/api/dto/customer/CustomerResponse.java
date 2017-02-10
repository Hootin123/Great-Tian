package com.xtr.api.dto.customer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersStationBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/18 19:15
 */
public class CustomerResponse implements Serializable {
    //返回消息
    private ResultResponse resultResponse;
    //企业总员工数
    private Integer totalCount;
    //入职人数
    private Integer enterCount;
    //离职人数
    private Integer leaveCount;
    //待转正人数
    private Integer willCount;
    //未补全资料人数
    private Integer lessCount;
    //试用期人数
    private Integer tryCount;
    //员工信息
    private CustomersBean customersBean;
    //员工岗位信息
    private CustomersStationBean customerStation;
    //当前工资基数
    private BigDecimal currentSalary;
    //未生效的工资基数
    private BigDecimal futureSalary;
    //未生效的调新日期
    private String futureDate;
    //周期ID
    private Long payrollId;
    //员工社保公积金基本信息
    private CustomerShebaoBean customerShebaoBean;
    //企业社保公积金订单信息
    private CompanyShebaoOrderBean companyShebaoOrderBean;

    //社保是否缴纳 1是 2否
    private Integer shebaoState;
    //公积金是否缴纳 1是 2否
    private Integer gjjState;
    //企业社保公积金订单是否提交 1已提交或者已关闭 2未提交或未到截止日
    private Integer companyState;
    //当前月(yyyyMM)
    private Integer currentMonth;
    //社保停缴月份(yyyyMM)
    private Integer shebaoTjMonth;
    //公积金停缴月(yyyyMM)
    private Integer gjjTjMonth;
    //离职前台社保显示的类型 1未缴纳 2显示单选框 3系统自动停缴提示 4账单提交自动停缴 5已设置社保停缴提示
    private Integer shebaoDimissShowType;
    //离职前台公积金显示的类型 1未缴纳 2
    private Integer gjjDimissShowType;
    //显示将要停缴的月份
    private Integer willTjMonth;

    public Integer getWillTjMonth() {
        return willTjMonth;
    }

    public void setWillTjMonth(Integer willTjMonth) {
        this.willTjMonth = willTjMonth;
    }

    public Integer getShebaoDimissShowType() {
        return shebaoDimissShowType;
    }

    public void setShebaoDimissShowType(Integer shebaoDimissShowType) {
        this.shebaoDimissShowType = shebaoDimissShowType;
    }

    public Integer getGjjDimissShowType() {
        return gjjDimissShowType;
    }

    public void setGjjDimissShowType(Integer gjjDimissShowType) {
        this.gjjDimissShowType = gjjDimissShowType;
    }

    public Integer getShebaoState() {
        return shebaoState;
    }

    public void setShebaoState(Integer shebaoState) {
        this.shebaoState = shebaoState;
    }

    public Integer getGjjState() {
        return gjjState;
    }

    public void setGjjState(Integer gjjState) {
        this.gjjState = gjjState;
    }

    public Integer getCompanyState() {
        return companyState;
    }

    public void setCompanyState(Integer companyState) {
        this.companyState = companyState;
    }

    public Integer getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(Integer currentMonth) {
        this.currentMonth = currentMonth;
    }

    public Integer getShebaoTjMonth() {
        return shebaoTjMonth;
    }

    public void setShebaoTjMonth(Integer shebaoTjMonth) {
        this.shebaoTjMonth = shebaoTjMonth;
    }

    public Integer getGjjTjMonth() {
        return gjjTjMonth;
    }

    public void setGjjTjMonth(Integer gjjTjMonth) {
        this.gjjTjMonth = gjjTjMonth;
    }

    public CustomerShebaoBean getCustomerShebaoBean() {
        return customerShebaoBean;
    }

    public void setCustomerShebaoBean(CustomerShebaoBean customerShebaoBean) {
        this.customerShebaoBean = customerShebaoBean;
    }

    public CompanyShebaoOrderBean getCompanyShebaoOrderBean() {
        return companyShebaoOrderBean;
    }

    public void setCompanyShebaoOrderBean(CompanyShebaoOrderBean companyShebaoOrderBean) {
        this.companyShebaoOrderBean = companyShebaoOrderBean;
    }

    public Long getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Long payrollId) {
        this.payrollId = payrollId;
    }

    public CustomersBean getCustomersBean() {
        return customersBean;
    }

    public void setCustomersBean(CustomersBean customersBean) {
        this.customersBean = customersBean;
    }

    public CustomersStationBean getCustomerStation() {
        return customerStation;
    }

    public void setCustomerStation(CustomersStationBean customerStation) {
        this.customerStation = customerStation;
    }

    public BigDecimal getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(BigDecimal currentSalary) {
        this.currentSalary = currentSalary;
    }

    public BigDecimal getFutureSalary() {
        return futureSalary;
    }

    public void setFutureSalary(BigDecimal futureSalary) {
        this.futureSalary = futureSalary;
    }

    public String getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(String futureDate) {
        this.futureDate = futureDate;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public ResultResponse getResultResponse() {
        return resultResponse;
    }

    public void setResultResponse(ResultResponse resultResponse) {
        this.resultResponse = resultResponse;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Integer enterCount) {
        this.enterCount = enterCount;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    public Integer getWillCount() {
        return willCount;
    }

    public void setWillCount(Integer willCount) {
        this.willCount = willCount;
    }

    public Integer getLessCount() {
        return lessCount;
    }

    public void setLessCount(Integer lessCount) {
        this.lessCount = lessCount;
    }
}
