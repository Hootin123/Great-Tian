package com.xtr.api.dto.shebao;

import java.io.Serializable;
import java.util.Date;

/**
 * 社保历史账单查询封装类
 * @author:zhangshuai
 * @date: 2016/9/29.
 */
public class HistoryBillDto  implements Serializable{

    //参保地区名字
    private String joinCityName;

    //账单月份
    private String orderDate;
    //城市的拼音
    private String joinCityCode;
    //公司名称模糊
    private String  companyName;
    //员工名称
    private String customerName;
    //部门id
    private String depId;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJoinCityCode() {
        return joinCityCode;
    }

    public void setJoinCityCode(String joinCityCode) {
        this.joinCityCode = joinCityCode;
    }

    public String getJoinCityName() {
        return joinCityName;
    }

    public void setJoinCityName(String joinCityName) {
        this.joinCityName = joinCityName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
