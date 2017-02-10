package com.xtr.api.dto.gateway.request;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.ParaName;
import com.xtr.api.dto.gateway.base.RequestPara;

/**
 * Created by xuewu on 2016/8/3.
 * 代付接口函数
 */
public class DefrayPayRequest extends RequestPara {

    //*收款银行编码
    @ParaName("payee_bank_code")
    private String payeeBankCode;

    //*收款帐户类型  对私户=P；对公户=C
    @ParaName("payee_account_type")
    private String payeeAccountType;

    //*货币类型，固定填CNY
    @ParaName("trade_currency")
    private String tradeCurrency;

    //*支付工具  固定值ACCT，XJK，TRAN任意一种。 ACCT代付到余额；XJK代付到小金库；TRAN代付到银行卡。
    @ParaName("pay_tool")
    private String payTool;

    //*收款帐户号
    @ParaName("payee_account_no")
    private String payeeAccountNo;

    //*收款帐户名称
    @ParaName("payee_account_name")
    private String payeeAccountName;

    //*商户订单流水号
    @ParaName("out_trade_no")
    private String outTradeNo;

    //*订单交易金额  单位：分，大于0。
    @ParaName("trade_amount")
    private String tradeAmount;

    //*订单摘要  商品描述，订单标题，关键描述信息
    @ParaName("trade_subject")
    private String tradeSubject;

    //*收款卡种  借记卡=DE；信用卡=CR
    @ParaName("payee_card_type")
    private String payeeCardType;

    //-收款银行全称
    @ParaName("payee_bank_fullname")
    private String payeeBankFullname;

    //-回调通知地址
    @ParaName("notify_url")
    private String notifyUrl;

    //-持卡人手机
    @ParaName("payee_mobile")
    private String payeeMobile;

    //-业务订单流水号
    @ParaName("biz_trade_no")
    private String bizTradeNo;

    //-联行号， 对公时必填！
    @ParaName("payee_bank_associated_code")
    private String payeeBankAssociatedCode;


    public DefrayPayRequest(BusinessType businessType) {
        this.businessType = businessType;
    }

    @Override
    public String getBusinessId() {
        return outTradeNo;
    }

    public String getPayeeBankCode() {
        return payeeBankCode;
    }

    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public String getPayTool() {
        return payTool;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public String getPayeeAccountNo() {
        return payeeAccountNo;
    }

    public void setPayeeAccountNo(String payeeAccountNo) {
        this.payeeAccountNo = payeeAccountNo;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeSubject() {
        return tradeSubject;
    }

    public void setTradeSubject(String tradeSubject) {
        this.tradeSubject = tradeSubject;
    }

    public String getPayeeCardType() {
        return payeeCardType;
    }

    public void setPayeeCardType(String payeeCardType) {
        this.payeeCardType = payeeCardType;
    }

    public String getPayeeBankFullname() {
        return payeeBankFullname;
    }

    public void setPayeeBankFullname(String payeeBankFullname) {
        this.payeeBankFullname = payeeBankFullname;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getBizTradeNo() {
        return bizTradeNo;
    }

    public void setBizTradeNo(String bizTradeNo) {
        this.bizTradeNo = bizTradeNo;
    }

    public String getPayeeBankAssociatedCode() {
        return payeeBankAssociatedCode;
    }

    public void setPayeeBankAssociatedCode(String payeeBankAssociatedCode) {
        this.payeeBankAssociatedCode = payeeBankAssociatedCode;
    }
}
