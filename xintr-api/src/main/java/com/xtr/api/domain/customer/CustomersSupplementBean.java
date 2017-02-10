package com.xtr.api.domain.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by allycw3 on 2016/9/28.
 */
public class CustomersSupplementBean implements Serializable {

    //
    private Long supplementId ;
    //员工ID
    private Long supplementCustomerId ;
    //企业社保公积金账单ID
    private Long supplementCompanyOrderId ;
    //社保企业补收费用
    private BigDecimal supplementSbCompanyReceive ;
    //社保企业补退费用
    private BigDecimal supplementSbCompanyBack;
    //社保个人补收费用
    private BigDecimal supplementSbSelfReceive;
    //社保个人补退费用
    private BigDecimal supplementSbSelfBack;
    //公积金企业补收费用
    private BigDecimal supplementGjjCompanyReceive;
    //公积金企业补退费用
    private BigDecimal supplementGjjCompanyBack;
    //公积金个人补收费用
    private BigDecimal supplementGjjSelfReceive;
    //公积金个人补退费用
    private BigDecimal supplementGjjSelfBack;
    //社保补收详细
    private String supplementSbReceiveDetail;
    //社保补退详细
    private String supplementSbBackDetail;
    //公积金补收详细
    private String supplementGjjReceiveDetail;
    //公积金补退详细
    private String supplementGjjBackDetail;
    //社保补收原因
    private String supplementSbReceiveReason;
    //社保补退原因
    private String supplementSbBackReason;
    //公积金补收原因
    private String supplementGjjReceiveReason;
    //公积金补退原因
    private String supplementGjjBackReason;
    //创建时间
    private Date supplementCreateTime;
    //是否补退到员工账户 1 是 2 否
    private Integer supplementIsBack;
    //总补退金额
    private BigDecimal supplementBackTotal;
    //支付错误信息
    private String supplementFailMsg;
    //支付状态  1失败过
    private Integer supplementPayStatus;
    //财务操作人
    private String supplementFinanceUser;
    //备注
    private String supplementRemark;

    public String getSupplementFailMsg() {
        return supplementFailMsg;
    }

    public void setSupplementFailMsg(String supplementFailMsg) {
        this.supplementFailMsg = supplementFailMsg;
    }

    public Integer getSupplementPayStatus() {
        return supplementPayStatus;
    }

    public void setSupplementPayStatus(Integer supplementPayStatus) {
        this.supplementPayStatus = supplementPayStatus;
    }

    public String getSupplementFinanceUser() {
        return supplementFinanceUser;
    }

    public void setSupplementFinanceUser(String supplementFinanceUser) {
        this.supplementFinanceUser = supplementFinanceUser;
    }

    public String getSupplementRemark() {
        return supplementRemark;
    }

    public void setSupplementRemark(String supplementRemark) {
        this.supplementRemark = supplementRemark;
    }

    public BigDecimal getSupplementBackTotal() {
        return supplementBackTotal;
    }

    public void setSupplementBackTotal(BigDecimal supplementBackTotal) {
        this.supplementBackTotal = supplementBackTotal;
    }

    public Integer getSupplementIsBack() {
        return supplementIsBack;
    }

    public void setSupplementIsBack(Integer supplementIsBack) {
        this.supplementIsBack = supplementIsBack;
    }

    public Long getSupplementId() {
        return supplementId;
    }

    public void setSupplementId(Long supplementId) {
        this.supplementId = supplementId;
    }

    public Long getSupplementCustomerId() {
        return supplementCustomerId;
    }

    public void setSupplementCustomerId(Long supplementCustomerId) {
        this.supplementCustomerId = supplementCustomerId;
    }

    public Long getSupplementCompanyOrderId() {
        return supplementCompanyOrderId;
    }

    public void setSupplementCompanyOrderId(Long supplementCompanyOrderId) {
        this.supplementCompanyOrderId = supplementCompanyOrderId;
    }

    public BigDecimal getSupplementSbCompanyReceive() {
        return supplementSbCompanyReceive;
    }

    public void setSupplementSbCompanyReceive(BigDecimal supplementSbCompanyReceive) {
        this.supplementSbCompanyReceive = supplementSbCompanyReceive;
    }

    public BigDecimal getSupplementSbCompanyBack() {
        return supplementSbCompanyBack;
    }

    public void setSupplementSbCompanyBack(BigDecimal supplementSbCompanyBack) {
        this.supplementSbCompanyBack = supplementSbCompanyBack;
    }

    public BigDecimal getSupplementSbSelfReceive() {
        return supplementSbSelfReceive;
    }

    public void setSupplementSbSelfReceive(BigDecimal supplementSbSelfReceive) {
        this.supplementSbSelfReceive = supplementSbSelfReceive;
    }

    public BigDecimal getSupplementSbSelfBack() {
        return supplementSbSelfBack;
    }

    public void setSupplementSbSelfBack(BigDecimal supplementSbSelfBack) {
        this.supplementSbSelfBack = supplementSbSelfBack;
    }

    public BigDecimal getSupplementGjjCompanyReceive() {
        return supplementGjjCompanyReceive;
    }

    public void setSupplementGjjCompanyReceive(BigDecimal supplementGjjCompanyReceive) {
        this.supplementGjjCompanyReceive = supplementGjjCompanyReceive;
    }

    public BigDecimal getSupplementGjjCompanyBack() {
        return supplementGjjCompanyBack;
    }

    public void setSupplementGjjCompanyBack(BigDecimal supplementGjjCompanyBack) {
        this.supplementGjjCompanyBack = supplementGjjCompanyBack;
    }

    public BigDecimal getSupplementGjjSelfReceive() {
        return supplementGjjSelfReceive;
    }

    public void setSupplementGjjSelfReceive(BigDecimal supplementGjjSelfReceive) {
        this.supplementGjjSelfReceive = supplementGjjSelfReceive;
    }

    public BigDecimal getSupplementGjjSelfBack() {
        return supplementGjjSelfBack;
    }

    public void setSupplementGjjSelfBack(BigDecimal supplementGjjSelfBack) {
        this.supplementGjjSelfBack = supplementGjjSelfBack;
    }

    public String getSupplementSbReceiveDetail() {
        return supplementSbReceiveDetail;
    }

    public void setSupplementSbReceiveDetail(String supplementSbReceiveDetail) {
        this.supplementSbReceiveDetail = supplementSbReceiveDetail;
    }

    public String getSupplementSbBackDetail() {
        return supplementSbBackDetail;
    }

    public void setSupplementSbBackDetail(String supplementSbBackDetail) {
        this.supplementSbBackDetail = supplementSbBackDetail;
    }

    public String getSupplementGjjReceiveDetail() {
        return supplementGjjReceiveDetail;
    }

    public void setSupplementGjjReceiveDetail(String supplementGjjReceiveDetail) {
        this.supplementGjjReceiveDetail = supplementGjjReceiveDetail;
    }

    public String getSupplementGjjBackDetail() {
        return supplementGjjBackDetail;
    }

    public void setSupplementGjjBackDetail(String supplementGjjBackDetail) {
        this.supplementGjjBackDetail = supplementGjjBackDetail;
    }

    public String getSupplementSbReceiveReason() {
        return supplementSbReceiveReason;
    }

    public void setSupplementSbReceiveReason(String supplementSbReceiveReason) {
        this.supplementSbReceiveReason = supplementSbReceiveReason;
    }

    public String getSupplementSbBackReason() {
        return supplementSbBackReason;
    }

    public void setSupplementSbBackReason(String supplementSbBackReason) {
        this.supplementSbBackReason = supplementSbBackReason;
    }

    public String getSupplementGjjReceiveReason() {
        return supplementGjjReceiveReason;
    }

    public void setSupplementGjjReceiveReason(String supplementGjjReceiveReason) {
        this.supplementGjjReceiveReason = supplementGjjReceiveReason;
    }

    public String getSupplementGjjBackReason() {
        return supplementGjjBackReason;
    }

    public void setSupplementGjjBackReason(String supplementGjjBackReason) {
        this.supplementGjjBackReason = supplementGjjBackReason;
    }

    public Date getSupplementCreateTime() {
        return supplementCreateTime;
    }

    public void setSupplementCreateTime(Date supplementCreateTime) {
        this.supplementCreateTime = supplementCreateTime;
    }
}
