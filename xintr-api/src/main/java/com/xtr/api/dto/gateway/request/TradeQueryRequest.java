package com.xtr.api.dto.gateway.request;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.ParaName;
import com.xtr.api.dto.gateway.base.RequestPara;

/**
 * Created by xuewu on 2016/8/3.
 * 代付接口函数
 */
public class TradeQueryRequest extends RequestPara {
    //*商户订单流水号
    @ParaName("out_trade_no")
    private String outTradeNo;

    //-商户订单流水号
    @ParaName("trade_no")
    private String tradeNo;

    //*商户订单流水号
    @ParaName("trade_type")
    private String tradeType = "T_AGD";

    public TradeQueryRequest(BusinessType businessType){
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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String getBusinessId() {
        return outTradeNo;
    }
}
