package com.xtr.api.domain.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SubAccountBean implements Serializable{
    /**
     *  主键,所属表字段为sub_account.id
     */
    private Long id;

    /**
     *  客户编号   安全控制值,所属表字段为sub_account.cust_id
     */
    private Long custId;

    /**
     *  总金额   安全控制值,所属表字段为sub_account.amout
     */
    private BigDecimal amout;

    /**
     *  可提现金额  安全控制值,所属表字段为sub_account.cash_amout
     */
    private BigDecimal cashAmout;

    /**
     *  不可提现金额  安全控制值,所属表字段为sub_account.uncash_amount
     */
    private BigDecimal uncashAmount;

    /**
     *  可提现冻结金额   安全控制值,所属表字段为sub_account.freeze_cash_amount
     */
    private BigDecimal freezeCashAmount;

    /**
     *  不可提现冻结金额   安全控制值,所属表字段为sub_account.freeze_unCash_amount
     */
    private BigDecimal freezeUncashAmount;

    /**
     *  账户性质 1-个人 2-企业,所属表字段为sub_account.property
     */
    private Integer property;

    /**
     *  状态 00-生效,01-冻结,02-注销,所属表字段为sub_account.state
     */
    private String state;

    /**
     *  创建时间,所属表字段为sub_account.create_time
     */
    private Date createTime;

    /**
     *  ,所属表字段为sub_account.update_time
     */
    private Date updateTime;

    /**
     *  校验码   对安全控制值MD5后，Base64编码,所属表字段为sub_account.check_value
     */
    private String checkValue;

    /**
     * 账户安全加密MD5值
     */
    private String hash;

    /**
     * 余额是否自动转入银行卡  0:非自动 1;自动
     */
    private Integer isAutoTransfer;

    /**
     * 获取 主键 字段:sub_account.id
     *
     * @return sub_account.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:sub_account.id
     *
     * @param id sub_account.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 客户编号   安全控制值 字段:sub_account.cust_id
     *
     * @return sub_account.cust_id, 客户编号   安全控制值
     */
    public Long getCustId() {
        return custId;
    }

    /**
     * 设置 客户编号   安全控制值 字段:sub_account.cust_id
     *
     * @param custId sub_account.cust_id, 客户编号   安全控制值
     */
    public void setCustId(Long custId) {
        this.custId = custId;
    }

    /**
     * 获取 总金额   安全控制值 字段:sub_account.amout
     *
     * @return sub_account.amout, 总金额   安全控制值
     */
    public BigDecimal getAmout() {
        return amout;
    }

    /**
     * 设置 总金额   安全控制值 字段:sub_account.amout
     *
     * @param amout sub_account.amout, 总金额   安全控制值
     */
    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }

    /**
     * 获取 可提现金额  安全控制值 字段:sub_account.cash_amout
     *
     * @return sub_account.cash_amout, 可提现金额  安全控制值
     */
    public BigDecimal getCashAmout() {
        return cashAmout;
    }

    /**
     * 设置 可提现金额  安全控制值 字段:sub_account.cash_amout
     *
     * @param cashAmout sub_account.cash_amout, 可提现金额  安全控制值
     */
    public void setCashAmout(BigDecimal cashAmout) {
        this.cashAmout = cashAmout;
    }

    /**
     * 获取 不可提现金额  安全控制值 字段:sub_account.uncash_amount
     *
     * @return sub_account.uncash_amount, 不可提现金额  安全控制值
     */
    public BigDecimal getUncashAmount() {
        return uncashAmount;
    }

    /**
     * 设置 不可提现金额  安全控制值 字段:sub_account.uncash_amount
     *
     * @param uncashAmount sub_account.uncash_amount, 不可提现金额  安全控制值
     */
    public void setUncashAmount(BigDecimal uncashAmount) {
        this.uncashAmount = uncashAmount;
    }

    /**
     * 获取 可提现冻结金额   安全控制值 字段:sub_account.freeze_cash_amount
     *
     * @return sub_account.freeze_cash_amount, 可提现冻结金额   安全控制值
     */
    public BigDecimal getFreezeCashAmount() {
        return freezeCashAmount;
    }

    /**
     * 设置 可提现冻结金额   安全控制值 字段:sub_account.freeze_cash_amount
     *
     * @param freezeCashAmount sub_account.freeze_cash_amount, 可提现冻结金额   安全控制值
     */
    public void setFreezeCashAmount(BigDecimal freezeCashAmount) {
        this.freezeCashAmount = freezeCashAmount;
    }

    /**
     * 获取 不可提现冻结金额   安全控制值 字段:sub_account.freeze_unCash_amount
     *
     * @return sub_account.freeze_unCash_amount, 不可提现冻结金额   安全控制值
     */
    public BigDecimal getFreezeUncashAmount() {
        return freezeUncashAmount;
    }

    /**
     * 设置 不可提现冻结金额   安全控制值 字段:sub_account.freeze_unCash_amount
     *
     * @param freezeUncashAmount sub_account.freeze_unCash_amount, 不可提现冻结金额   安全控制值
     */
    public void setFreezeUncashAmount(BigDecimal freezeUncashAmount) {
        this.freezeUncashAmount = freezeUncashAmount;
    }

    /**
     * 获取 账户性质 1-个人 2-企业 字段:sub_account.property
     *
     * @return sub_account.property, 账户性质 1-个人 2-企业
     */
    public Integer getProperty() {
        return property;
    }

    /**
     * 设置 账户性质 1-个人 2-企业 字段:sub_account.property
     *
     * @param property sub_account.property, 账户性质 1-个人 2-企业
     */
    public void setProperty(Integer property) {
        this.property = property;
    }

    /**
     * 获取 状态 00-生效,01-冻结,02-注销 字段:sub_account.state
     *
     * @return sub_account.state, 状态 00-生效,01-冻结,02-注销
     */
    public String getState() {
        return state;
    }

    /**
     * 设置 状态 00-生效,01-冻结,02-注销 字段:sub_account.state
     *
     * @param state sub_account.state, 状态 00-生效,01-冻结,02-注销
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * 获取 创建时间 字段:sub_account.create_time
     *
     * @return sub_account.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:sub_account.create_time
     *
     * @param createTime sub_account.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取  字段:sub_account.update_time
     *
     * @return sub_account.update_time, 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置  字段:sub_account.update_time
     *
     * @param updateTime sub_account.update_time, 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 校验码   对安全控制值MD5后，Base64编码 字段:sub_account.check_value
     *
     * @return sub_account.check_value, 校验码   对安全控制值MD5后，Base64编码
     */
    public String getCheckValue() {
        return checkValue;
    }

    /**
     * 设置 校验码   对安全控制值MD5后，Base64编码 字段:sub_account.check_value
     *
     * @param checkValue sub_account.check_value, 校验码   对安全控制值MD5后，Base64编码
     */
    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue == null ? null : checkValue.trim();
    }


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getIsAutoTransfer() {
        return isAutoTransfer;
    }

    public void setIsAutoTransfer(Integer isAutoTransfer) {
        this.isAutoTransfer = isAutoTransfer;
    }
}