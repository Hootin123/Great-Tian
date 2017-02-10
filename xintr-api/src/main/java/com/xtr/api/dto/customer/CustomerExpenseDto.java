package com.xtr.api.dto.customer;

import com.xtr.api.basic.BaseObject;
import com.xtr.api.domain.customer.CustomerExpenseDetailBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报销列表dto
 * @Author Xuewu
 * @Date 2016/9/5.
 */
public class CustomerExpenseDto extends BaseObject implements Serializable {
    private Long payCycleId;
    private Long companyId;
    private Integer stationEmployMethod;
    private Integer stationCustomerState;
    private Long deptId;
    private String userName;
    private Integer isExpense;
    private Date payCycleStartDate;

    private Long customerId;

    private List<CustomerExpenseDetailBean> beans;

    private List<Long> deleteId;

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

    public Integer getStationEmployMethod() {
        return stationEmployMethod;
    }

    public void setStationEmployMethod(Integer stationEmployMethod) {
        this.stationEmployMethod = stationEmployMethod;
    }

    public Integer getStationCustomerState() {
        return stationCustomerState;
    }

    public void setStationCustomerState(Integer stationCustomerState) {
        this.stationCustomerState = stationCustomerState;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CustomerExpenseDetailBean> getBeans() {
        return beans;
    }

    public void setBeans(List<CustomerExpenseDetailBean> beans) {
        this.beans = beans;
    }

    public List<Long> getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(List<Long> deleteId) {
        this.deleteId = deleteId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getIsExpense() {
        return isExpense;
    }

    public void setIsExpense(Integer isExpense) {
        this.isExpense = isExpense;
    }

    public Date getPayCycleStartDate() {
        return payCycleStartDate;
    }

    public void setPayCycleStartDate(Date payCycleStartDate) {
        this.payCycleStartDate = payCycleStartDate;
    }
}
