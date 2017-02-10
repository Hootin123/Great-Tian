package com.xtr.api.dto.customer;

import com.xtr.api.domain.customer.CustomerRechargesBean;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/15 14:08
 */
public class CustomerReachrgeDto extends CustomerRechargesBean implements Serializable {

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 员工姓名
     */
    private String uname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 财务经办人
     */
    private String auditName;

    /**
     * 企业id
     */
    private Long companyId;

    public CustomerReachrgeDto() {
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
