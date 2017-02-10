package com.xtr.api.dto.gateway.base;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by xuewu on 2016/8/3.
 */
public abstract class ResponsePara extends BasePara {


    //    提交者会员号	customer_no
    @ParaName("customer_no")
    protected String customerNo;

    //    响应时间	response_datetime
    @ParaName("response_datetime")
    protected String responseDatetime;

    //    响应编码	response_code
    @ParaName("response_code")
    protected String responseCode;

    //    响应描述	responseMessage
    @ParaName("response_message")
    protected String responseMessage;

    //    签名方式	sign_type
    @ParaName("sign_type")
    protected String signDype;

    //    签名数据	sign_data
    @ParaName("sign_data")
    protected String signData;

    //- 交易回传
    @ParaName("return_params")
    private String returnParams;

    @Override
    public int getLogType() {
        return 1;
    }

    @Override
    public String getApiName() {
        return null;
    }

    /**
     * 设置jd返回结果
     * @param result
     * @throws Exception
     */
    public void setResult(Map result) throws Exception {
        if (result != null) {
            List<CachedMethod> methodList = getAllDeclaredMethod();
            for (CachedMethod cachedGetterMethod : methodList) {
                String reqName = cachedGetterMethod.getReqName();
                if (result.containsKey(reqName)) {
                    try {
                        Method setterMethod = cachedGetterMethod.getSetterMethod();
                        setterMethod.invoke(this, String.valueOf(result.get(reqName)));
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }

    }


    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getResponseDatetime() {
        return responseDatetime;
    }

    public void setResponseDatetime(String responseDatetime) {
        this.responseDatetime = responseDatetime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getSignDype() {
        return signDype;
    }

    public void setSignDype(String signDype) {
        this.signDype = signDype;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(String returnParams) {
        this.returnParams = returnParams;
    }
}
