package com.xtr.comm.jd.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by xuewu on 2016/8/3.
 */
@Component
public class JdPayConfig implements Serializable {

    @Value("${jdpay.base_url}")
    private String baseUrl;

    //代付
    @Value("${jdpay.defrayPay_url}")
    private String defrayPayUrl;

    //交易查询
    @Value("${jdpay.trade_query_url}")
    private String tradeQueryUrl;

    //秘钥文件密码
    @Value("${jdpay.pri_pass_word}")
    private String priPassWord;

    //秘钥文件名
    @Value("${jdpay.pri_file_name}")
    private String priFileName;

    //京东证书
    @Value("${jdpay.pub_file_name}")
    private String pubFileName;

    //签名秘钥
    @Value("${jdpay.sing_key}")
    private String singKey;

    //异步回调baseUrl
    @Value("${jdpay.notify_base_url}")
    private String notifyBaseUrl;

    //企业提现异步回调
    @Value("${jdpay.company_withdraw_notify_url}")
    private String companyWithdrawNotifyUrl;

    //员工提现异步回调
    @Value("${jdpay.customer_withdraw_notify_url}")
    private String customerWithdrawNotifyUrl;

    //测试客户异步回调
    @Value("${jdpay.customer_validate_notify_url}")
    private String customerValidateNotifyUrl;

    @Value("${jdpay.rapidly_withdraw_notify_url}")
    private String rapidlyWithdrawNotifyUrl;

    //customer_no
    @Value("${jdpay.customer_no}")
    private String customerNo;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getDefrayPayUrl() {
        return defrayPayUrl;
    }

    public String getTradeQueryUrl() {
        return tradeQueryUrl;
    }

    public String getPriPassWord() {
        return priPassWord;
    }

    public String getPriFileName() {
        return priFileName;
    }

    public String getPubFileName() {
        return pubFileName;
    }

    public String getSingKey() {
        return singKey;
    }

    public String getNotifyBaseUrl() {
        return notifyBaseUrl;
    }

    public String getCompanyWithdrawNotifyUrl() {
        return companyWithdrawNotifyUrl;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public String getCustomerWithdrawNotifyUrl() {
        return customerWithdrawNotifyUrl;
    }

    public String getCustomerValidateNotifyUrl() {
        return customerValidateNotifyUrl;
    }

    public String getRapidlyWithdrawNotifyUrl() {
        return rapidlyWithdrawNotifyUrl;
    }
}
