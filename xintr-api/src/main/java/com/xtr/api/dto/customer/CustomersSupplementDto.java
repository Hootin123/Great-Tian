package com.xtr.api.dto.customer;

import com.xtr.api.domain.customer.CustomersSupplementBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by allycw3 on 2016/9/28.
 */
public class CustomersSupplementDto extends CustomersSupplementBean implements Serializable {

    //员工名称
    private String memberName;
    //员工手机号
    private String memberPhone;
    //部门编号
    private Long deptId;
    //部门名称
    private String deptName;
    //地区编码
    private String areaCode;
    //地区名称
    private String areaName;
    //企业补收总费用
    private BigDecimal companyReceiveTotal;
    //企业补退总费用
    private BigDecimal companyBackTotal;
    //个人补收总费用
    private BigDecimal selfReceiveTotal;
    //个人补退总费用
    private BigDecimal selfBackTotal;
    //企业补差总费用
    private BigDecimal companyDiffTotal;
    //自己对象实例集合
    private List<CustomersSupplementDto> supplementDtos;
    //企业社保补差费用
    private BigDecimal companySbDiff;
    //个人社保补差费用
    private BigDecimal selfSbDiff;
    //企业公积金补差费用
    private BigDecimal companyGjjDiff;
    //个人公积金补差费用
    private BigDecimal selfGjjDiff;
    //缴纳月份
    private String payMonth;
    //补差明细名称
    private String payName;
    //补差类型
    private String payType;
    //个人部份
    private BigDecimal paySelfAmount;
    //企业部份
    private BigDecimal payCompanyAmount;
    //备注
    private String payRemark;
    //界面展示社保补差费用
    private BigDecimal sbDiffInfo;
    //界面展示公积金补差费用
    private BigDecimal gjjDiffInfo;

    public BigDecimal getSbDiffInfo() {
        return sbDiffInfo;
    }

    public void setSbDiffInfo(BigDecimal sbDiffInfo) {
        this.sbDiffInfo = sbDiffInfo;
    }

    public BigDecimal getGjjDiffInfo() {
        return gjjDiffInfo;
    }

    public void setGjjDiffInfo(BigDecimal gjjDiffInfo) {
        this.gjjDiffInfo = gjjDiffInfo;
    }

    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getPaySelfAmount() {
        return paySelfAmount;
    }

    public void setPaySelfAmount(BigDecimal paySelfAmount) {
        this.paySelfAmount = paySelfAmount;
    }

    public BigDecimal getPayCompanyAmount() {
        return payCompanyAmount;
    }

    public void setPayCompanyAmount(BigDecimal payCompanyAmount) {
        this.payCompanyAmount = payCompanyAmount;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark;
    }

    public BigDecimal getCompanySbDiff() {
        return companySbDiff;
    }

    public void setCompanySbDiff(BigDecimal companySbDiff) {
        this.companySbDiff = companySbDiff;
    }

    public BigDecimal getSelfSbDiff() {
        return selfSbDiff;
    }

    public void setSelfSbDiff(BigDecimal selfSbDiff) {
        this.selfSbDiff = selfSbDiff;
    }

    public BigDecimal getCompanyGjjDiff() {
        return companyGjjDiff;
    }

    public void setCompanyGjjDiff(BigDecimal companyGjjDiff) {
        this.companyGjjDiff = companyGjjDiff;
    }

    public BigDecimal getSelfGjjDiff() {
        return selfGjjDiff;
    }

    public void setSelfGjjDiff(BigDecimal selfGjjDiff) {
        this.selfGjjDiff = selfGjjDiff;
    }

    public List<CustomersSupplementDto> getSupplementDtos() {
        return supplementDtos;
    }

    public void setSupplementDtos(List<CustomersSupplementDto> supplementDtos) {
        this.supplementDtos = supplementDtos;
    }

    public BigDecimal getCompanyReceiveTotal() {
        return companyReceiveTotal;
    }

    public void setCompanyReceiveTotal(BigDecimal companyReceiveTotal) {
        this.companyReceiveTotal = companyReceiveTotal;
    }

    public BigDecimal getCompanyBackTotal() {
        return companyBackTotal;
    }

    public void setCompanyBackTotal(BigDecimal companyBackTotal) {
        this.companyBackTotal = companyBackTotal;
    }

    public BigDecimal getSelfReceiveTotal() {
        return selfReceiveTotal;
    }

    public void setSelfReceiveTotal(BigDecimal selfReceiveTotal) {
        this.selfReceiveTotal = selfReceiveTotal;
    }

    public BigDecimal getSelfBackTotal() {
        return selfBackTotal;
    }

    public void setSelfBackTotal(BigDecimal selfBackTotal) {
        this.selfBackTotal = selfBackTotal;
    }

    public BigDecimal getCompanyDiffTotal() {
        return companyDiffTotal;
    }

    public void setCompanyDiffTotal(BigDecimal companyDiffTotal) {
        this.companyDiffTotal = companyDiffTotal;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
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

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
