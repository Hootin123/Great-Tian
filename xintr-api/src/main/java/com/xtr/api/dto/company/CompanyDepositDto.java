package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyDepositBean;

import java.io.Serializable;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/5 18:47
 */
public class CompanyDepositDto extends CompanyDepositBean implements Serializable {

    //企业名称
    private String companyName;
    //企业手机号
    private String companyContactTel;
    //用户名
    private String memberLogName;
    //审核状态
    private Integer approveState;
    //认证状态
    private Integer authState;
    //签约状态
    private Integer ptotocolState;
    //企业ID
    private Long companyId;
    //用户ID
    private Long memberId;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getApproveState() {
        return approveState;
    }

    public void setApproveState(Integer approveState) {
        this.approveState = approveState;
    }

    public Integer getAuthState() {
        return authState;
    }

    public void setAuthState(Integer authState) {
        this.authState = authState;
    }

    public Integer getPtotocolState() {
        return ptotocolState;
    }

    public void setPtotocolState(Integer ptotocolState) {
        this.ptotocolState = ptotocolState;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMemberLogName() {
        return memberLogName;
    }

    public void setMemberLogName(String memberLogName) {
        this.memberLogName = memberLogName;
    }

    public String getCompanyContactTel() {
        return companyContactTel;
    }

    public void setCompanyContactTel(String companyContactTel) {
        this.companyContactTel = companyContactTel;
    }
}
