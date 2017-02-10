package com.xtr.api.dto.customer;

import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderDescBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/19 17:33.
 */
public class CustomerShebaoOrderDto extends CustomerShebaoOrderDescBean implements Serializable {

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 员工姓名
     */
    private String memberName;

    /**
     * 员工手机号码
     */
    private String telphone;

    /**
     * 社保基数
     */
    private BigDecimal shebaoBase;

    /**
     * 公积金基数
     */
    private BigDecimal gjjBase;

    /**
     * 社保order
     */
    private CustomerShebaoOrderBean customberOrder;

    /**
     * 补缴列表
     */
    private List<CustomerShebaoOrderBean> bjList;
    //判断是否提交账单 1已经提交账单 2未提交账单
    private Integer isSubmitAccount;


    private  Integer orderType;

    private Date overdueMonth ;

    public Date getOverdueMonth() {
        return overdueMonth;
    }

    public void setOverdueMonth(Date overdueMonth) {
        this.overdueMonth = overdueMonth;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }


    public Integer getIsSubmitAccount() {
        return isSubmitAccount;
    }

    public void setIsSubmitAccount(Integer isSubmitAccount) {
        this.isSubmitAccount = isSubmitAccount;
    }

    public CustomerShebaoOrderDto() {
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public CustomerShebaoOrderBean getCustomberOrder() {
        return customberOrder;
    }

    public void setCustomberOrder(CustomerShebaoOrderBean customberOrder) {
        this.customberOrder = customberOrder;
    }

    public List<CustomerShebaoOrderBean> getBjList() {
        return bjList;
    }

    public void setBjList(List<CustomerShebaoOrderBean> bjList) {
        this.bjList = bjList;
    }

    public BigDecimal getShebaoBase() {
        return shebaoBase;
    }

    public void setShebaoBase(BigDecimal shebaoBase) {
        this.shebaoBase = shebaoBase;
    }

    public BigDecimal getGjjBase() {
        return gjjBase;
    }

    public void setGjjBase(BigDecimal gjjBase) {
        this.gjjBase = gjjBase;
    }
}
