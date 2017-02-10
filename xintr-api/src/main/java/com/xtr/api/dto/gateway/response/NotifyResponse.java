package com.xtr.api.dto.gateway.response;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.ParaName;
import com.xtr.api.dto.gateway.base.ResponsePara;

/**
 * Created by xuewu on 2016/8/4.
 */
public class NotifyResponse extends ResponsePara {

    //商户账户号	customer_no
    @ParaName("merchant_no")
    protected String merchantNo;

    //商户订单流水号	out_trade_no
    @ParaName("out_trade_no")
    protected String outTradeNo;

    //业务订单流水号	biz_trade_no
    @ParaName("biz_trade_no")
    protected String bizTradeNo;

    //外部交易日	out_trade_date
    @ParaName("out_trade_date")
    protected String outTradeDate;

    //交易类别	trade_class
    @ParaName("trade_class")
    protected String tradeClass;

    //订单摘要	trade_subject
    @ParaName("trade_subject")
    protected String tradeSubject;

    //交易号	trade_no
    @ParaName("trade_no")
    protected String tradeNo;

    //卖方信息	seller_info
    @ParaName("seller_info")
    protected String sellerInfo;

    //买方信息	buyer_info
    @ParaName("buyer_info")
    protected String buyerInfo;

    //订单交易金额	trade_amount
    @ParaName("trade_amount")
    protected String tradeAmount;

    //订单交易币种	trade_currency
    @ParaName("trade_currency")
    protected String tradeCurrency;

    //已退款金额	refund_amount
    @ParaName("refund_amount")
    protected String refundAmount;

    //商户售卖的商品分类码	category_code
    @ParaName("category_code")
    protected String categoryCode;

    //已确认金额	confirm_amount
    @ParaName("confirm_amount")
    protected String confirmAmount;

    //交易响应码	trade_respcode
    @ParaName("trade_respcode")
    protected String tradeRespcode;

    //交易响应描述	trade_respmsg
    @ParaName("trade_respmsg")
    protected String tradeRespmsg;

    //交易状态	trade_status
    @ParaName("trade_status")
    protected String tradeStatus;

    //支付完成日	trade_pay_date
    @ParaName("trade_pay_date")
    protected String tradePayDate;

    //支付完时间	trade_pay_time
    @ParaName("trade_pay_time")
    protected String tradePayTime;

    //支付工具	pay_tool
    @ParaName("pay_tool")
    protected String payTool;

    //支付银行	bank_code
    @ParaName("bank_code")
    protected String bankCode;

    //支付卡种	card_type
    @ParaName("card_type")
    protected String cardType;

    //交易完成日	trade_finish_date
    @ParaName("trade_finish_date")
    protected String tradeFinishDate;

    //交易完时间	trade_finish_time
    @ParaName("trade_finish_time")
    protected String tradeFinishTime;

    public NotifyResponse(BusinessType businessType) {
        this.businessType = businessType;
    }


    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBizTradeNo() {
        return bizTradeNo;
    }

    public void setBizTradeNo(String bizTradeNo) {
        this.bizTradeNo = bizTradeNo;
    }

    public String getOutTradeDate() {
        return outTradeDate;
    }

    public void setOutTradeDate(String outTradeDate) {
        this.outTradeDate = outTradeDate;
    }

    public String getTradeClass() {
        return tradeClass;
    }

    public void setTradeClass(String tradeClass) {
        this.tradeClass = tradeClass;
    }

    public String getTradeSubject() {
        return tradeSubject;
    }

    public void setTradeSubject(String tradeSubject) {
        this.tradeSubject = tradeSubject;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(String sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public String getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(String buyerInfo) {
        this.buyerInfo = buyerInfo;
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

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getConfirmAmount() {
        return confirmAmount;
    }

    public void setConfirmAmount(String confirmAmount) {
        this.confirmAmount = confirmAmount;
    }

    public String getTradeRespcode() {
        return tradeRespcode;
    }

    public void setTradeRespcode(String tradeRespcode) {
        this.tradeRespcode = tradeRespcode;
    }

    public String getTradeRespmsg() {
        return tradeRespmsg;
    }

    public void setTradeRespmsg(String tradeRespmsg) {
        this.tradeRespmsg = tradeRespmsg;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradePayDate() {
        return tradePayDate;
    }

    public void setTradePayDate(String tradePayDate) {
        this.tradePayDate = tradePayDate;
    }

    public String getTradePayTime() {
        return tradePayTime;
    }

    public void setTradePayTime(String tradePayTime) {
        this.tradePayTime = tradePayTime;
    }

    public String getPayTool() {
        return payTool;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTradeFinishDate() {
        return tradeFinishDate;
    }

    public void setTradeFinishDate(String tradeFinishDate) {
        this.tradeFinishDate = tradeFinishDate;
    }

    public String getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(String tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }

    @Override
    public String getBusinessId() {
        return outTradeNo;
    }
}
