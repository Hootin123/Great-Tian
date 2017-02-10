package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanysBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/24 14:17
 */

public class CompanysDto extends CompanysBean implements Serializable {


    /**
     * 部门主键
     */
    private Long depId;

    /**
     * 部门名称
     */
    private String depName;

    //账户余额
    private BigDecimal totalAmount;
    //签约状态字符串 1代发协议 2垫发协议,由逗号相隔
    private String protocolState;
    //签约状态 1代发协议 2垫发协议
    private Integer protocolStateInt;

    // 父级部门id
    private Integer depParentId;


    /**
     * 部门下的人员数量
     */
    private int customerCount;
    //用户名
    private String memberLogname;
    //企业手机号
//    private String companyMobilePhone;
    //最近登录时间
    private Date memberLoginRecentTime;

    public Date getMemberLoginRecentTime() {
        return memberLoginRecentTime;
    }

    public void setMemberLoginRecentTime(Date memberLoginRecentTime) {
        this.memberLoginRecentTime = memberLoginRecentTime;
    }

//    public String getCompanyMobilePhone() {
//        return companyMobilePhone;
//    }
//
//    public void setCompanyMobilePhone(String companyMobilePhone) {
//        this.companyMobilePhone = companyMobilePhone;
//    }

    public String getMemberLogname() {
        return memberLogname;
    }

    public void setMemberLogname(String memberLogname) {
        this.memberLogname = memberLogname;
    }

    public Integer getProtocolStateInt() {
        return protocolStateInt;
    }

    public void setProtocolStateInt(Integer protocolStateInt) {
        this.protocolStateInt = protocolStateInt;
    }

    public String getProtocolState() {
        return protocolState;
    }

    public void setProtocolState(String protocolState) {
        this.protocolState = protocolState;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public Integer getDepParentId() {
        return depParentId;
    }

    public void setDepParentId(Integer depParentId) {
        this.depParentId = depParentId;
    }
}
