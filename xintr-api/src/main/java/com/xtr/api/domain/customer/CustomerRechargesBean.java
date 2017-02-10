package com.xtr.api.domain.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值提现
 */
public class CustomerRechargesBean extends BaseObject implements Serializable {
    /**
     *  id,所属表字段为customer_recharges.recharge_id
     */
    private Long rechargeId;

    /**
     *  类型 1充值 2提现,所属表字段为customer_recharges.recharge_type
     */
    private Integer rechargeType;

    /**
     *  充值金额,所属表字段为customer_recharges.recharge_money
     */
    private BigDecimal rechargeMoney;

    /**
     *  企业(用户)id,所属表字段为customer_recharges.recharge_customer_id
     */
    private Long rechargeCustomerId;

    /**
     *  创建时间,所属表字段为customer_recharges.recharge_addtime
     */
    private Date rechargeAddtime;

    /**
     *  编号,所属表字段为customer_recharges.recharge_number
     */
    private String rechargeNumber;

    /**
     * 提现批次编号,所属表字段为customer_recharges.batch_number
     */
    private String batchNumber;

    /**
     *  注备，如充值平台等,所属表字段为customer_recharges.recharge_bak
     */
    private String rechargeBak;

    /**
     *  第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银,所属表字段为customer_recharges.recharge_station
     */
    private Integer rechargeStation;

    /**
     *  客户端 1web 2wap 3ios 4android,所属表字段为customer_recharges.recharge_client
     */
    private Integer rechargeClient;

    /**
     *  服务费用,所属表字段为customer_recharges.recharge_moneyserver
     */
    private BigDecimal rechargeMoneyserver;

    /**
     *  银行,所属表字段为customer_recharges.recharge_bank
     */
    private String rechargeBank;

    /**
     *  地区,所属表字段为customer_recharges.recharge_bankarea
     */
    private String rechargeBankarea;

    /**
     *  卡号,所属表字段为customer_recharges.recharge_banknumber
     */
    private String rechargeBanknumber;

    /**
     *  最后实际到账,所属表字段为customer_recharges.recharge_moneynow
     */
    private BigDecimal rechargeMoneynow;

    /**
     *  是否导出 0否 1是,所属表字段为customer_recharges.recharge_istoexcel
     */
    private Integer rechargeIstoexcel;

    /**
     *  系统补贴,所属表字段为customer_recharges.recharge_moneybt
     */
    private BigDecimal rechargeMoneybt;

    /**
     *  是否体现到账户资金变化 0否 1是,所属表字段为customer_recharges.recharge_istorecord
     */
    private Integer rechargeIstorecord;

    /**
     *  回调时间(成功/失败) 充值才有,所属表字段为customer_recharges.recharge_recall_time
     */
    private Date rechargeRecallTime;

    /**
     *  回调结果 0待回调 1成功 2....各种错误定义,所属表字段为customer_recharges.recharge_recall_result
     */
    private Integer rechargeRecallResult;

    /**
     *  状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过,所属表字段为customer_recharges.recharge_state
     */
    private Integer rechargeState;

    /**
     *  初审时间,所属表字段为customer_recharges.recharge_audit_first_time
     */
    private Date rechargeAuditFirstTime;

    /**
     *  初审人,所属表字段为customer_recharges.recharge_audit_first_member
     */
    private Long rechargeAuditFirstMember;

    /**
     *  初审备注,所属表字段为customer_recharges.recharge_audit_first_remark
     */
    private String rechargeAuditFirstRemark;

    /**
     *  复审时间,所属表字段为customer_recharges.recharge_audit_second_time
     */
    private Date rechargeAuditSecondTime;

    /**
     *   复审备注,所属表字段为customer_recharges.recharge_audit_second_remark
     */
    private String rechargeAuditSecondRemark;

    /**
     *  复审人,所属表字段为customer_recharges.recharge_audit_second_member
     */
    private Long rechargeAuditSecondMember;

    /**
     * 来源id
     */
    private Long resourceId;

    /**
     *  获取 id 字段:customer_recharges.recharge_id 
     *
     *  @return customer_recharges.recharge_id, id
     */
    public Long getRechargeId() {
        return rechargeId;
    }

    /**
     *  设置 id 字段:customer_recharges.recharge_id 
     *
     *  @param rechargeId customer_recharges.recharge_id, id
     */
    public void setRechargeId(Long rechargeId) {
        this.rechargeId = rechargeId;
    }

    /**
     *  获取 类型 1充值 2提现 字段:customer_recharges.recharge_type 
     *
     *  @return customer_recharges.recharge_type, 类型 1充值 2提现
     */
    public Integer getRechargeType() {
        return rechargeType;
    }

    /**
     *  设置 类型 1充值 2提现 字段:customer_recharges.recharge_type 
     *
     *  @param rechargeType customer_recharges.recharge_type, 类型 1充值 2提现
     */
    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    /**
     *  获取 充值金额 字段:customer_recharges.recharge_money 
     *
     *  @return customer_recharges.recharge_money, 充值金额
     */
    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    /**
     *  设置 充值金额 字段:customer_recharges.recharge_money 
     *
     *  @param rechargeMoney customer_recharges.recharge_money, 充值金额
     */
    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    /**
     *  获取 企业(用户)id 字段:customer_recharges.recharge_customer_id 
     *
     *  @return customer_recharges.recharge_customer_id, 企业(用户)id
     */
    public Long getRechargeCustomerId() {
        return rechargeCustomerId;
    }

    /**
     *  设置 企业(用户)id 字段:customer_recharges.recharge_customer_id 
     *
     *  @param rechargeCustomerId customer_recharges.recharge_customer_id, 企业(用户)id
     */
    public void setRechargeCustomerId(Long rechargeCustomerId) {
        this.rechargeCustomerId = rechargeCustomerId;
    }

    /**
     *  获取 创建时间 字段:customer_recharges.recharge_addtime 
     *
     *  @return customer_recharges.recharge_addtime, 创建时间
     */
    public Date getRechargeAddtime() {
        return rechargeAddtime;
    }

    /**
     *  设置 创建时间 字段:customer_recharges.recharge_addtime 
     *
     *  @param rechargeAddtime customer_recharges.recharge_addtime, 创建时间
     */
    public void setRechargeAddtime(Date rechargeAddtime) {
        this.rechargeAddtime = rechargeAddtime;
    }

    /**
     *  获取 编号 字段:customer_recharges.recharge_number 
     *
     *  @return customer_recharges.recharge_number, 编号
     */
    public String getRechargeNumber() {
        return rechargeNumber;
    }

    /**
     *  设置 编号 字段:customer_recharges.recharge_number 
     *
     *  @param rechargeNumber customer_recharges.recharge_number, 编号
     */
    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber == null ? null : rechargeNumber.trim();
    }

    /**
     *  获取 注备，如充值平台等 字段:customer_recharges.recharge_bak 
     *
     *  @return customer_recharges.recharge_bak, 注备，如充值平台等
     */
    public String getRechargeBak() {
        return rechargeBak;
    }

    /**
     *  设置 注备，如充值平台等 字段:customer_recharges.recharge_bak 
     *
     *  @param rechargeBak customer_recharges.recharge_bak, 注备，如充值平台等
     */
    public void setRechargeBak(String rechargeBak) {
        this.rechargeBak = rechargeBak == null ? null : rechargeBak.trim();
    }

    /**
     *  获取 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银 字段:customer_recharges.recharge_station 
     *
     *  @return customer_recharges.recharge_station, 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
     */
    public Integer getRechargeStation() {
        return rechargeStation;
    }

    /**
     *  设置 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银 字段:customer_recharges.recharge_station 
     *
     *  @param rechargeStation customer_recharges.recharge_station, 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
     */
    public void setRechargeStation(Integer rechargeStation) {
        this.rechargeStation = rechargeStation;
    }

    /**
     *  获取 客户端 1web 2wap 3ios 4android 字段:customer_recharges.recharge_client 
     *
     *  @return customer_recharges.recharge_client, 客户端 1web 2wap 3ios 4android
     */
    public Integer getRechargeClient() {
        return rechargeClient;
    }

    /**
     *  设置 客户端 1web 2wap 3ios 4android 字段:customer_recharges.recharge_client 
     *
     *  @param rechargeClient customer_recharges.recharge_client, 客户端 1web 2wap 3ios 4android
     */
    public void setRechargeClient(Integer rechargeClient) {
        this.rechargeClient = rechargeClient;
    }

    /**
     *  获取 服务费用 字段:customer_recharges.recharge_moneyserver 
     *
     *  @return customer_recharges.recharge_moneyserver, 服务费用
     */
    public BigDecimal getRechargeMoneyserver() {
        return rechargeMoneyserver;
    }

    /**
     *  设置 服务费用 字段:customer_recharges.recharge_moneyserver 
     *
     *  @param rechargeMoneyserver customer_recharges.recharge_moneyserver, 服务费用
     */
    public void setRechargeMoneyserver(BigDecimal rechargeMoneyserver) {
        this.rechargeMoneyserver = rechargeMoneyserver;
    }

    /**
     *  获取 银行 字段:customer_recharges.recharge_bank 
     *
     *  @return customer_recharges.recharge_bank, 银行
     */
    public String getRechargeBank() {
        return rechargeBank;
    }

    /**
     *  设置 银行 字段:customer_recharges.recharge_bank 
     *
     *  @param rechargeBank customer_recharges.recharge_bank, 银行
     */
    public void setRechargeBank(String rechargeBank) {
        this.rechargeBank = rechargeBank == null ? null : rechargeBank.trim();
    }

    /**
     *  获取 地区 字段:customer_recharges.recharge_bankarea 
     *
     *  @return customer_recharges.recharge_bankarea, 地区
     */
    public String getRechargeBankarea() {
        return rechargeBankarea;
    }

    /**
     *  设置 地区 字段:customer_recharges.recharge_bankarea 
     *
     *  @param rechargeBankarea customer_recharges.recharge_bankarea, 地区
     */
    public void setRechargeBankarea(String rechargeBankarea) {
        this.rechargeBankarea = rechargeBankarea == null ? null : rechargeBankarea.trim();
    }

    /**
     *  获取 卡号 字段:customer_recharges.recharge_banknumber 
     *
     *  @return customer_recharges.recharge_banknumber, 卡号
     */
    public String getRechargeBanknumber() {
        return rechargeBanknumber;
    }

    /**
     *  设置 卡号 字段:customer_recharges.recharge_banknumber 
     *
     *  @param rechargeBanknumber customer_recharges.recharge_banknumber, 卡号
     */
    public void setRechargeBanknumber(String rechargeBanknumber) {
        this.rechargeBanknumber = rechargeBanknumber == null ? null : rechargeBanknumber.trim();
    }

    /**
     *  获取 最后实际到账 字段:customer_recharges.recharge_moneynow 
     *
     *  @return customer_recharges.recharge_moneynow, 最后实际到账
     */
    public BigDecimal getRechargeMoneynow() {
        return rechargeMoneynow;
    }

    /**
     *  设置 最后实际到账 字段:customer_recharges.recharge_moneynow 
     *
     *  @param rechargeMoneynow customer_recharges.recharge_moneynow, 最后实际到账
     */
    public void setRechargeMoneynow(BigDecimal rechargeMoneynow) {
        this.rechargeMoneynow = rechargeMoneynow;
    }

    /**
     *  获取 是否导出 0否 1是 字段:customer_recharges.recharge_istoexcel 
     *
     *  @return customer_recharges.recharge_istoexcel, 是否导出 0否 1是
     */
    public Integer getRechargeIstoexcel() {
        return rechargeIstoexcel;
    }

    /**
     *  设置 是否导出 0否 1是 字段:customer_recharges.recharge_istoexcel 
     *
     *  @param rechargeIstoexcel customer_recharges.recharge_istoexcel, 是否导出 0否 1是
     */
    public void setRechargeIstoexcel(Integer rechargeIstoexcel) {
        this.rechargeIstoexcel = rechargeIstoexcel;
    }

    /**
     *  获取 系统补贴 字段:customer_recharges.recharge_moneybt 
     *
     *  @return customer_recharges.recharge_moneybt, 系统补贴
     */
    public BigDecimal getRechargeMoneybt() {
        return rechargeMoneybt;
    }

    /**
     *  设置 系统补贴 字段:customer_recharges.recharge_moneybt 
     *
     *  @param rechargeMoneybt customer_recharges.recharge_moneybt, 系统补贴
     */
    public void setRechargeMoneybt(BigDecimal rechargeMoneybt) {
        this.rechargeMoneybt = rechargeMoneybt;
    }

    /**
     *  获取 是否体现到账户资金变化 0否 1是 字段:customer_recharges.recharge_istorecord 
     *
     *  @return customer_recharges.recharge_istorecord, 是否体现到账户资金变化 0否 1是
     */
    public Integer getRechargeIstorecord() {
        return rechargeIstorecord;
    }

    /**
     *  设置 是否体现到账户资金变化 0否 1是 字段:customer_recharges.recharge_istorecord 
     *
     *  @param rechargeIstorecord customer_recharges.recharge_istorecord, 是否体现到账户资金变化 0否 1是
     */
    public void setRechargeIstorecord(Integer rechargeIstorecord) {
        this.rechargeIstorecord = rechargeIstorecord;
    }

    /**
     *  获取 回调时间(成功/失败) 充值才有 字段:customer_recharges.recharge_recall_time 
     *
     *  @return customer_recharges.recharge_recall_time, 回调时间(成功/失败) 充值才有
     */
    public Date getRechargeRecallTime() {
        return rechargeRecallTime;
    }

    /**
     *  设置 回调时间(成功/失败) 充值才有 字段:customer_recharges.recharge_recall_time 
     *
     *  @param rechargeRecallTime customer_recharges.recharge_recall_time, 回调时间(成功/失败) 充值才有
     */
    public void setRechargeRecallTime(Date rechargeRecallTime) {
        this.rechargeRecallTime = rechargeRecallTime;
    }

    /**
     *  获取 回调结果 0待回调 1成功 2....各种错误定义 字段:customer_recharges.recharge_recall_result 
     *
     *  @return customer_recharges.recharge_recall_result, 回调结果 0待回调 1成功 2....各种错误定义
     */
    public Integer getRechargeRecallResult() {
        return rechargeRecallResult;
    }

    /**
     *  设置 回调结果 0待回调 1成功 2....各种错误定义 字段:customer_recharges.recharge_recall_result 
     *
     *  @param rechargeRecallResult customer_recharges.recharge_recall_result, 回调结果 0待回调 1成功 2....各种错误定义
     */
    public void setRechargeRecallResult(Integer rechargeRecallResult) {
        this.rechargeRecallResult = rechargeRecallResult;
    }

    /**
     *  获取 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 字段:customer_recharges.recharge_state 
     *
     *  @return customer_recharges.recharge_state, 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过
     */
    public Integer getRechargeState() {
        return rechargeState;
    }

    /**
     *  设置 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 字段:customer_recharges.recharge_state 
     *
     *  @param rechargeState customer_recharges.recharge_state, 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过
     */
    public void setRechargeState(Integer rechargeState) {
        this.rechargeState = rechargeState;
    }

    /**
     *  获取 初审时间 字段:customer_recharges.recharge_audit_first_time 
     *
     *  @return customer_recharges.recharge_audit_first_time, 初审时间
     */
    public Date getRechargeAuditFirstTime() {
        return rechargeAuditFirstTime;
    }

    /**
     *  设置 初审时间 字段:customer_recharges.recharge_audit_first_time 
     *
     *  @param rechargeAuditFirstTime customer_recharges.recharge_audit_first_time, 初审时间
     */
    public void setRechargeAuditFirstTime(Date rechargeAuditFirstTime) {
        this.rechargeAuditFirstTime = rechargeAuditFirstTime;
    }

    /**
     *  获取 初审人 字段:customer_recharges.recharge_audit_first_member 
     *
     *  @return customer_recharges.recharge_audit_first_member, 初审人
     */
    public Long getRechargeAuditFirstMember() {
        return rechargeAuditFirstMember;
    }

    /**
     *  设置 初审人 字段:customer_recharges.recharge_audit_first_member 
     *
     *  @param rechargeAuditFirstMember customer_recharges.recharge_audit_first_member, 初审人
     */
    public void setRechargeAuditFirstMember(Long rechargeAuditFirstMember) {
        this.rechargeAuditFirstMember = rechargeAuditFirstMember;
    }

    /**
     *  获取 初审备注 字段:customer_recharges.recharge_audit_first_remark 
     *
     *  @return customer_recharges.recharge_audit_first_remark, 初审备注
     */
    public String getRechargeAuditFirstRemark() {
        return rechargeAuditFirstRemark;
    }

    /**
     *  设置 初审备注 字段:customer_recharges.recharge_audit_first_remark 
     *
     *  @param rechargeAuditFirstRemark customer_recharges.recharge_audit_first_remark, 初审备注
     */
    public void setRechargeAuditFirstRemark(String rechargeAuditFirstRemark) {
        this.rechargeAuditFirstRemark = rechargeAuditFirstRemark == null ? null : rechargeAuditFirstRemark.trim();
    }

    /**
     *  获取 复审时间 字段:customer_recharges.recharge_audit_second_time 
     *
     *  @return customer_recharges.recharge_audit_second_time, 复审时间
     */
    public Date getRechargeAuditSecondTime() {
        return rechargeAuditSecondTime;
    }

    /**
     *  设置 复审时间 字段:customer_recharges.recharge_audit_second_time 
     *
     *  @param rechargeAuditSecondTime customer_recharges.recharge_audit_second_time, 复审时间
     */
    public void setRechargeAuditSecondTime(Date rechargeAuditSecondTime) {
        this.rechargeAuditSecondTime = rechargeAuditSecondTime;
    }

    /**
     *  获取  复审备注 字段:customer_recharges.recharge_audit_second_remark 
     *
     *  @return customer_recharges.recharge_audit_second_remark,  复审备注
     */
    public String getRechargeAuditSecondRemark() {
        return rechargeAuditSecondRemark;
    }

    /**
     *  设置  复审备注 字段:customer_recharges.recharge_audit_second_remark 
     *
     *  @param rechargeAuditSecondRemark customer_recharges.recharge_audit_second_remark,  复审备注
     */
    public void setRechargeAuditSecondRemark(String rechargeAuditSecondRemark) {
        this.rechargeAuditSecondRemark = rechargeAuditSecondRemark == null ? null : rechargeAuditSecondRemark.trim();
    }

    /**
     *  获取 复审人 字段:customer_recharges.recharge_audit_second_member 
     *
     *  @return customer_recharges.recharge_audit_second_member, 复审人
     */
    public Long getRechargeAuditSecondMember() {
        return rechargeAuditSecondMember;
    }

    /**
     *  设置 复审人 字段:customer_recharges.recharge_audit_second_member 
     *
     *  @param rechargeAuditSecondMember customer_recharges.recharge_audit_second_member, 复审人
     */
    public void setRechargeAuditSecondMember(Long rechargeAuditSecondMember) {
        this.rechargeAuditSecondMember = rechargeAuditSecondMember;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}