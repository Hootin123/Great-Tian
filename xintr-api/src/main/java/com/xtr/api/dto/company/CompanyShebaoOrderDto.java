package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyShebaoOrderBean;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/23 20:26.
 */
public class CompanyShebaoOrderDto extends CompanyShebaoOrderBean implements Serializable {

    //异常说明
    private String orderErrorText;
    //离提交订单截止日期天数
    private Integer diffDays;
    //企业名称
    private String companyName;
    //用户名称
    private String userName;
    //社保异常
    private String sbErrorText;
    //公积金异常
    private String gjjErrorText;
    //员工异常数量
    private Integer number;
    //订单编号
    private String rechargeNumber;

    public String getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSbErrorText() {
        return sbErrorText;
    }

    public void setSbErrorText(String sbErrorText) {
        this.sbErrorText = sbErrorText;
    }

    public String getGjjErrorText() {
        return gjjErrorText;
    }

    public void setGjjErrorText(String gjjErrorText) {
        this.gjjErrorText = gjjErrorText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(Integer diffDays) {
        this.diffDays = diffDays;
    }

    public CompanyShebaoOrderDto() {
    }

    public String getOrderErrorText() {
        return orderErrorText;
    }

    public void setOrderErrorText(String orderErrorText) {
        this.orderErrorText = orderErrorText;
    }
}
