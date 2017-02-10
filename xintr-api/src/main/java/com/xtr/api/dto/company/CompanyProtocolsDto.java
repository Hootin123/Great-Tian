package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyProtocolsBean;

import java.io.Serializable;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/18 16:32
 */
public class CompanyProtocolsDto extends CompanyProtocolsBean implements Serializable {
    //企业名称
    private String companyName;
    //法人电话
    private String companyCorporationPhone;
    //前台显示操作栏按钮 1 显示代发签约 2显示垫发签约 3不显示签约
    private Integer operationShowState;
    //企业ID
    private Long companyId;
    //有效期限
    private String useFulTime;
    //新增协议操作列
    private Integer addProtocolShowState;
    //用户名
    private String memberLogname;
    //企业手机号
    private String companyContactTel;
    //签约申请状态 1已签约 2未签约 3已提交签约申请
    private Integer applyState;

    //状态名称
    private String applyStateStr;

    //签约名称
    private String protocolContractTypeStr;

    //签约时间
    private String applyTimeStr;

    //签约的链接
    private String isNeedApply;
    //签约产品说明
    private String produceComment;

    public String getProduceComment() {
        return produceComment;
    }

    public void setProduceComment(String produceComment) {
        this.produceComment = produceComment;
    }

    public String getApplyStateStr() {
        return applyStateStr;
    }

    public void setApplyStateStr(String applyStateStr) {
        this.applyStateStr = applyStateStr;
    }

    public String getProtocolContractTypeStr() {
        return protocolContractTypeStr;
    }

    public void setProtocolContractTypeStr(String protocolContractTypeStr) {
        this.protocolContractTypeStr = protocolContractTypeStr;
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
    }

    public String getIsNeedApply() {
        return isNeedApply;
    }

    public void setIsNeedApply(String isNeedApply) {
        this.isNeedApply = isNeedApply;
    }

    public Integer getApplyState() {
        return applyState;
    }

    public void setApplyState(Integer applyState) {
        this.applyState = applyState;
    }

    public String getMemberLogname() {
        return memberLogname;
    }

    public void setMemberLogname(String memberLogname) {
        this.memberLogname = memberLogname;
    }

    public String getCompanyContactTel() {
        return companyContactTel;
    }

    public void setCompanyContactTel(String companyContactTel) {
        this.companyContactTel = companyContactTel;
    }

    public Integer getAddProtocolShowState() {
        return addProtocolShowState;
    }

    public void setAddProtocolShowState(Integer addProtocolShowState) {
        this.addProtocolShowState = addProtocolShowState;
    }

    public String getUseFulTime() {
        return useFulTime;
    }

    public void setUseFulTime(String useFulTime) {
        this.useFulTime = useFulTime;
    }

    public Integer getOperationShowState() {
        return operationShowState;
    }

    public void setOperationShowState(Integer operationShowState) {
        this.operationShowState = operationShowState;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCorporationPhone() {
        return companyCorporationPhone;
    }

    public void setCompanyCorporationPhone(String companyCorporationPhone) {
        this.companyCorporationPhone = companyCorporationPhone;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
