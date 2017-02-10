package com.xtr.api.dto.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:zhangshuai
 * @date: 2016/11/14.
 */
public class CustomerWechatDto implements Serializable {

    //员工id
    private Long customerId;
    //员工姓名
    private String  name;
    //性别
    private Integer customerSex;
    //生日
    private Date customerBirthday;
    //员工工号
    private String customerNumber;
    //部门名称
    private String depName;
    //岗位
    private String stationStationName;
    //入职日期
    private Date stationEnterTime;
    //当前基本工资
    private BigDecimal customerCurrentSalary;
    //参保地区
    private String  joinCityName;
    //社保基数
    private BigDecimal sbBase;
    //公积金基数
    private BigDecimal gjjBase;

    //身份证正面
    private String customerIdcardImgFront;
    //身份证背面
    private String customerIdcardImgBack;

    //身份证正面复制
    private String  customerIdcardImgFrontCopy;

    //身份证背面复制
    private String  customerIdcardImgBackCopy;

    //银行
    private String customerBank;
    //身份证号
    private String customerIdcard;

    private String customerBanknumber;

    //资料是否补全
    private  Integer customerIsComplement;
    //手机号码
    private String  customerPhone;

    //字符串生日
    private  String   customerBirthdayMonth;

    public String getCustomerBirthdayMonth() {
        return customerBirthdayMonth==null?"":customerBirthdayMonth.replace("-",".");
    }

    public void setCustomerBirthdayMonth(String customerBirthdayMonth) {
        this.customerBirthdayMonth = customerBirthdayMonth;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerBanknumber() {
        return customerBanknumber;
    }

    public void setCustomerBanknumber(String customerBanknumber) {
        this.customerBanknumber = customerBanknumber;
    }

    public String getCustomerIdcard() {
        return customerIdcard;
    }

    public void setCustomerIdcard(String customerIdcard) {
        this.customerIdcard = customerIdcard;
    }

    public Integer getCustomerIsComplement() {
        return customerIsComplement;
    }

    public void setCustomerIsComplement(Integer customerIsComplement) {
        this.customerIsComplement = customerIsComplement;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(Integer customerSex) {
        this.customerSex = customerSex;
    }

    public Date getCustomerBirthday() {
        return customerBirthday;
    }

    public void setCustomerBirthday(Date customerBirthday) {
        this.customerBirthday = customerBirthday;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getStationStationName() {
        return stationStationName;
    }

    public void setStationStationName(String stationStationName) {
        this.stationStationName = stationStationName;
    }

    public Date getStationEnterTime() {
        return stationEnterTime;
    }

    public void setStationEnterTime(Date stationEnterTime) {
        this.stationEnterTime = stationEnterTime;
    }

    public BigDecimal getCustomerCurrentSalary() {
        return customerCurrentSalary;
    }

    public void setCustomerCurrentSalary(BigDecimal customerCurrentSalary) {
        this.customerCurrentSalary = customerCurrentSalary;
    }

    public String getJoinCityName() {
        return joinCityName;
    }

    public void setJoinCityName(String joinCityName) {
        this.joinCityName = joinCityName;
    }

    public BigDecimal getSbBase() {
        return sbBase;
    }

    public void setSbBase(BigDecimal sbBase) {
        this.sbBase = sbBase;
    }

    public BigDecimal getGjjBase() {
        return gjjBase;
    }

    public void setGjjBase(BigDecimal gjjBase) {
        this.gjjBase = gjjBase;
    }

    public String getCustomerIdcardImgFront()

    {
        return customerIdcardImgFront==null?"":customerIdcardImgFront;
    }

    public void setCustomerIdcardImgFront(String customerIdcardImgFront) {
        this.customerIdcardImgFront = customerIdcardImgFront;
    }

    public String getCustomerIdcardImgBack() {

        return customerIdcardImgBack==null?"":customerIdcardImgBack;
    }

    public void setCustomerIdcardImgBack(String customerIdcardImgBack) {
        this.customerIdcardImgBack = customerIdcardImgBack;
    }

    public String getCustomerBank() {
        return customerBank;
    }

    public void setCustomerBank(String customerBank) {
        this.customerBank = customerBank;
    }

    public String getCustomerIdcardImgFrontCopy() {
        return customerIdcardImgFrontCopy;
    }

    public void setCustomerIdcardImgFrontCopy(String customerIdcardImgFrontCopy) {
        this.customerIdcardImgFrontCopy = customerIdcardImgFrontCopy;
    }

    public String getCustomerIdcardImgBackCopy() {
        return customerIdcardImgBackCopy;
    }

    public void setCustomerIdcardImgBackCopy(String customerIdcardImgBackCopy) {
        this.customerIdcardImgBackCopy = customerIdcardImgBackCopy;
    }
}
