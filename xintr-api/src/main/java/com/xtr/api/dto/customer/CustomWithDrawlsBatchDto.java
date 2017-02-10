package com.xtr.api.dto.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>个人提现批次</p>
 *
 * @author 任齐
 * @createTime: 2016/7/13 20:09
 */
public class CustomWithDrawlsBatchDto extends BaseObject implements Serializable {

    /**
     * 批次编号
     */
    private String batchNumber;

    /**
     * 首次申请时间
     */
    private Date firstTime;

    /**
     * 最晚申请时间
     */
    private Date endTime;

    /**
     * 批次总金额
     */
    private BigDecimal batchMoney;

    /**
     * 批次总请求
     */
    private Integer batchCount;

    /**
     * 审核状态
     */
    private Integer rechargeState;

    /**
     * 财务审核人
     */
    private String auditName;

    public CustomWithDrawlsBatchDto() {
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getBatchMoney() {
        return batchMoney;
    }

    public void setBatchMoney(BigDecimal batchMoney) {
        this.batchMoney = batchMoney;
    }

    public Integer getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(Integer batchCount) {
        this.batchCount = batchCount;
    }

    public Integer getRechargeState() {
        return rechargeState;
    }

    public void setRechargeState(Integer rechargeState) {
        this.rechargeState = rechargeState;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }
}
