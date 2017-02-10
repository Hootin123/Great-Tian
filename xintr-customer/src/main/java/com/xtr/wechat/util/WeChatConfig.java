package com.xtr.wechat.util;

import com.xtr.comm.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author Xuewu
 * @Date 2016/9/2.
 */
@Component
public class WeChatConfig implements Serializable {

    public WeChatConfig(){
        System.out.println();
    }


    //    #公众号 appid
    @Value("${wechat.appId}")
    private String appId;

    //    #公众号 秘钥
    @Value("${wechat.appSecret}")
    private String appSecret;

    //    #公众号 token
    @Value("${wechat.token}")
    private String token;

    //            #发红包接口
    @Value("${wechat.sendRedPackUrl}")
    private String wxurlString;


    //            #支付账号 秘钥
    @Value("${wechat.shoppaykey}")
    private String shoppaykey; //微信支付密钥

    //    #支付账号 ID
    @Value("${wechat.shoppayid}")
    private String shoppayid;//微信支付商户号

    //    #获取OpenId
    @Value("${wechat.oauthUrl}")
    private String oauthUrl;

    //            #获取 公众号 access token
    @Value("${wechat.getAccessTokenUrl}")
    private String getAccessTokenUrl;

    //            #获取用户信息
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    public String getAppId() {
        return PropertyUtils.getString("wechat.appId");
    }

    public String getAppSecret() {
        return PropertyUtils.getString("wechat.appSecret");
    }

    public String getToken() {
        return PropertyUtils.getString("wechat.token");
    }

    public String getWxurlString() {
        return PropertyUtils.getString("wechat.sendRedPackUrl");
    }

    public String getShoppaykey() {
        return PropertyUtils.getString("wechat.shoppaykey");
    }

    public String getShoppayid() {
        return PropertyUtils.getString("wechat.shoppayid");
    }

    public String getOauthUrl() {
        return PropertyUtils.getString("wechat.oauthUrl");
    }

    public String getGetAccessTokenUrl() {
        return PropertyUtils.getString("wechat.getAccessTokenUrl");
    }

    public String getUserInfoUrl() {
        return PropertyUtils.getString("wechat.userInfoUrl");
    }
}
