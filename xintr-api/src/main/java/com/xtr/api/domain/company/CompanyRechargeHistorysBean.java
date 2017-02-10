package com.xtr.api.domain.company;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>企业充值提现历史</p>
 *
 * @author 任齐
 * @createTime: 2016/06/29 上午10:26:33
 */
public class CompanyRechargeHistorysBean {
    /**
     *  id,所属表字段为company_recharge_historys.history_id
     */
    private Long historyId;

    /**
     *  企业id,所属表字段为company_recharge_historys.history_company_id
     */
    private Long historyCompanyId;

    /**
     *  银行,所属表字段为company_recharge_historys.history_bank
     */
    private String historyBank;

    /**
     *  卡号,所属表字段为company_recharge_historys.history_banknumber
     */
    private String historyBanknumber;

    /**
     *  银行地址,所属表字段为company_recharge_historys.history_bankarea
     */
    private String historyBankarea;

    /**
     *  充值来源,所属表字段为company_recharge_historys.history_comesource
     */
    private String historyComesource;

    /**
     *  历史充值金额,所属表字段为company_recharge_historys.history_recharge_money
     */
    private BigDecimal historyRechargeMoney;

    /**
     *  历史提现金额,所属表字段为company_recharge_historys.history_withdraw_money
     */
    private BigDecimal historyWithdrawMoney;

    /**
     *  最后一次充值时间,所属表字段为company_recharge_historys.history_recharge_time
     */
    private Date historyRechargeTime;

    /**
     *  最后一次提现时间,所属表字段为company_recharge_historys.hsitory_withdraw_time
     */
    private Date hsitoryWithdrawTime;

    /**
     * 获取 id 字段:company_recharge_historys.history_id
     *
     * @return company_recharge_historys.history_id, id
     */
    public Long getHistoryId() {
        return historyId;
    }

    /**
     * 设置 id 字段:company_recharge_historys.history_id
     *
     * @param historyId company_recharge_historys.history_id, id
     */
    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    /**
     * 获取 企业id 字段:company_recharge_historys.history_company_id
     *
     * @return company_recharge_historys.history_company_id, 企业id
     */
    public Long getHistoryCompanyId() {
        return historyCompanyId;
    }

    /**
     * 设置 企业id 字段:company_recharge_historys.history_company_id
     *
     * @param historyCompanyId company_recharge_historys.history_company_id, 企业id
     */
    public void setHistoryCompanyId(Long historyCompanyId) {
        this.historyCompanyId = historyCompanyId;
    }

    /**
     * 获取 银行 字段:company_recharge_historys.history_bank
     *
     * @return company_recharge_historys.history_bank, 银行
     */
    public String getHistoryBank() {
        return historyBank;
    }

    /**
     * 设置 银行 字段:company_recharge_historys.history_bank
     *
     * @param historyBank company_recharge_historys.history_bank, 银行
     */
    public void setHistoryBank(String historyBank) {
        this.historyBank = historyBank == null ? null : historyBank.trim();
    }

    /**
     * 获取 卡号 字段:company_recharge_historys.history_banknumber
     *
     * @return company_recharge_historys.history_banknumber, 卡号
     */
    public String getHistoryBanknumber() {
        return historyBanknumber;
    }

    /**
     * 设置 卡号 字段:company_recharge_historys.history_banknumber
     *
     * @param historyBanknumber company_recharge_historys.history_banknumber, 卡号
     */
    public void setHistoryBanknumber(String historyBanknumber) {
        this.historyBanknumber = historyBanknumber == null ? null : historyBanknumber.trim();
    }

    /**
     * 获取 银行地址 字段:company_recharge_historys.history_bankarea
     *
     * @return company_recharge_historys.history_bankarea, 银行地址
     */
    public String getHistoryBankarea() {
        return historyBankarea;
    }

    /**
     * 设置 银行地址 字段:company_recharge_historys.history_bankarea
     *
     * @param historyBankarea company_recharge_historys.history_bankarea, 银行地址
     */
    public void setHistoryBankarea(String historyBankarea) {
        this.historyBankarea = historyBankarea == null ? null : historyBankarea.trim();
    }

    /**
     * 获取 充值来源 字段:company_recharge_historys.history_comesource
     *
     * @return company_recharge_historys.history_comesource, 充值来源
     */
    public String getHistoryComesource() {
        return historyComesource;
    }

    /**
     * 设置 充值来源 字段:company_recharge_historys.history_comesource
     *
     * @param historyComesource company_recharge_historys.history_comesource, 充值来源
     */
    public void setHistoryComesource(String historyComesource) {
        this.historyComesource = historyComesource == null ? null : historyComesource.trim();
    }

    /**
     * 获取 历史充值金额 字段:company_recharge_historys.history_recharge_money
     *
     * @return company_recharge_historys.history_recharge_money, 历史充值金额
     */
    public BigDecimal getHistoryRechargeMoney() {
        return historyRechargeMoney;
    }

    /**
     * 设置 历史充值金额 字段:company_recharge_historys.history_recharge_money
     *
     * @param historyRechargeMoney company_recharge_historys.history_recharge_money, 历史充值金额
     */
    public void setHistoryRechargeMoney(BigDecimal historyRechargeMoney) {
        this.historyRechargeMoney = historyRechargeMoney;
    }

    /**
     * 获取 历史提现金额 字段:company_recharge_historys.history_withdraw_money
     *
     * @return company_recharge_historys.history_withdraw_money, 历史提现金额
     */
    public BigDecimal getHistoryWithdrawMoney() {
        return historyWithdrawMoney;
    }

    /**
     * 设置 历史提现金额 字段:company_recharge_historys.history_withdraw_money
     *
     * @param historyWithdrawMoney company_recharge_historys.history_withdraw_money, 历史提现金额
     */
    public void setHistoryWithdrawMoney(BigDecimal historyWithdrawMoney) {
        this.historyWithdrawMoney = historyWithdrawMoney;
    }

    /**
     * 获取 最后一次充值时间 字段:company_recharge_historys.history_recharge_time
     *
     * @return company_recharge_historys.history_recharge_time, 最后一次充值时间
     */
    public Date getHistoryRechargeTime() {
        return historyRechargeTime;
    }

    /**
     * 设置 最后一次充值时间 字段:company_recharge_historys.history_recharge_time
     *
     * @param historyRechargeTime company_recharge_historys.history_recharge_time, 最后一次充值时间
     */
    public void setHistoryRechargeTime(Date historyRechargeTime) {
        this.historyRechargeTime = historyRechargeTime;
    }

    /**
     * 获取 最后一次提现时间 字段:company_recharge_historys.hsitory_withdraw_time
     *
     * @return company_recharge_historys.hsitory_withdraw_time, 最后一次提现时间
     */
    public Date getHsitoryWithdrawTime() {
        return hsitoryWithdrawTime;
    }

    /**
     * 设置 最后一次提现时间 字段:company_recharge_historys.hsitory_withdraw_time
     *
     * @param hsitoryWithdrawTime company_recharge_historys.hsitory_withdraw_time, 最后一次提现时间
     */
    public void setHsitoryWithdrawTime(Date hsitoryWithdrawTime) {
        this.hsitoryWithdrawTime = hsitoryWithdrawTime;
    }
}