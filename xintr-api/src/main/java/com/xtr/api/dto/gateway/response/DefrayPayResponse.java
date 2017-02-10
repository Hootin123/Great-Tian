package com.xtr.api.dto.gateway.response;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.ParaName;
import com.xtr.api.dto.gateway.base.RequestPara;
import com.xtr.api.dto.gateway.base.ResponsePara;

/**
 * Created by xuewu on 2016/8/3.
 * 代付接口函数
 */
public class DefrayPayResponse extends ResponsePara {
    //商户订单流水号	out_trade_no
    @ParaName("out_trade_no")
    private String outTradeNo;

    //交易号	trade_no
    @ParaName("trade_no")
    private String tradeNo;

    //交易状态	trade_status
    @ParaName("trade_status")
    private String tradeStatus;

    //订单交易金额	trade_amount
    @ParaName("trade_amount")
    private String tradeAmount;

    //币种	trade_currency
    @ParaName("trade_currency")
    private String tradeCurrency;

    public DefrayPayResponse(BusinessType businessType) {
        this.businessType = businessType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    @Override
    public String getBusinessId() {
        return outTradeNo;
    }
}
