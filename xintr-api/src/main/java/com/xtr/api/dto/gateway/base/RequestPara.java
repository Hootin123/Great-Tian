package com.xtr.api.dto.gateway.base;

import com.alibaba.fastjson.JSON;
import com.xtr.comm.basic.BusinessException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xuewu on 2016/8/3.
 */
public abstract class RequestPara extends BasePara {
    //    提交者会员号	customer_no
    @ParaName("customer_no")
    protected String customerNo;

    //    签名方式	sign_type
    @ParaName("sign_type")
    protected String signType;

    //*请求时间 yyyymmddTHH24MMSS
    @ParaName("request_datetime")
    private String requestDatetime;

    //- 交易回传
    @ParaName("return_params")
    private String returnParams;



    @Override
    public int getLogType() {
        return 0;
    }

    @Override
    public String getLogContent() {
        return JSON.toJSONString(createReqMap(true));
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getRequestDatetime() {
        return requestDatetime;
    }

    public void setRequestDatetime(String requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public String getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(String returnParams) {
        this.returnParams = returnParams;
    }
}
