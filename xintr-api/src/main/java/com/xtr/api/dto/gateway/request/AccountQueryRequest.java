package com.xtr.api.dto.gateway.request;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.ParaName;
import com.xtr.api.dto.gateway.base.RequestPara;

/**
 * Created by xuewu on 2016/8/3.
 * 代付接口函数
 */
public class AccountQueryRequest extends RequestPara {
    //*商户订单流水号
    @ParaName("out_trade_no")
    private String outTradeNo = "0";

    //-商户订单流水号
    @ParaName("buyer_info")
    private String buyer_info;

    //*商户订单流水号
    @ParaName("query_type")
    private String queryType = "BUSINESS_BASIC";

    //*商户订单流水号
    @ParaName("ledger_type")
    private String ledgerType = "00";

    public AccountQueryRequest(){
        this.businessType = BusinessType.QUERY_ACCOUNT;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBuyer_info() {
        return String.format("{\"customer_code\":\"%s\",\"customer_type\":\"CUSTOMER_NO\"}", customerNo);
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getLedgerType() {
        return ledgerType;
    }

    public void setLedgerType(String ledgerType) {
        this.ledgerType = ledgerType;
    }
}
