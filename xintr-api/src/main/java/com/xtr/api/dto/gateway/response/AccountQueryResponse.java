package com.xtr.api.dto.gateway.response;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.ParaName;
import com.xtr.api.dto.gateway.base.ResponsePara;

/**
 * Created by xuewu on 2016/8/3.
 * 代付接口函数
 */
public class AccountQueryResponse extends ResponsePara {

    //商户订单流水号	out_trade_no
    @ParaName("out_trade_no")
    private String outTradeNo;

    //交易号	buyer_info
    @ParaName("buyer_info")
    private String buyerInfo;

    //账户总金额	account_amount
    @ParaName("account_amount")
    private String accountAmount;

    //冻结总余额	frozen_amount
    @ParaName("frozen_amount")
    private String frozenAmount;

    //币种	trade_currency
    @ParaName("trade_currency")
    private String tradeCurrency;

    public AccountQueryResponse(){
        this.businessType = BusinessType.QUERY_ACCOUNT;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(String buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(String frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }
}
