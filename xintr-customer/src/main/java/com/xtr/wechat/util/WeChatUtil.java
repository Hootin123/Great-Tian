package com.xtr.wechat.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author Xuewu
 * @Date 2016/8/25.
 */
@Component
public class WeChatUtil {

    //测试
//    public static final String appId = "wx5cf94567a1553afe";
//    public static final String appSecret = "77e63ee0a82bb3ee9e8c491bb68008e8";
//    public static final String token = "xuewudestoken";

    //正式

    @Autowired
    private WeChatConfig weChatConfig;

    public Timer cleanTokenTimer = new Timer();

    private String access_token;

    public synchronized String getAccessToken(){
        if(access_token != null) {
            return access_token;
        }
        Map<String, String> para = new LinkedHashMap<>();
        para.put("grant_type", "client_credential");
        para.put("appid", weChatConfig.getAppId());
        para.put("secret", weChatConfig.getAppSecret());
        try {
            Map map = doGet(weChatConfig.getGetAccessTokenUrl(), para);
            access_token = (String) map.get("access_token");
            Integer expires_in = (Integer) map.get("expires_in");
            if(null != expires_in){
                cleanTokenTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (WeChatUtil.class){
                            access_token = null;
                        }
                    }
                }, Long.valueOf(expires_in));
            }
            return access_token;
        } catch (IOException e) {
            return null;
        }
    }

    public String checkSignature(String timestamp, String nonce) {

        ArrayList<String> list = new ArrayList<String>();
        list.add(weChatConfig.getToken());
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        String tempStr = list.get(0) + list.get(1) + list.get(2);
        return SHA1(tempStr);
    }

    public String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Map doOauth2(String code) throws IOException {
        Map<String, String> para = new LinkedHashMap<>();
        para.put("code", code);
        para.put("appid", weChatConfig.getAppId());
        para.put("secret", weChatConfig.getAppSecret());
        para.put("grant_type", "authorization_code");
        return doGet(weChatConfig.getOauthUrl(), para);
    }

    public Map userInfo(String openId) throws IOException {
        Map<String, String> para = new LinkedHashMap<>();
        para.put("access_token", getAccessToken());
        para.put("openid", openId);
        para.put("lang", "zh_CN");
        return doGet(weChatConfig.getUserInfoUrl(), para);
    }

    private Map doGet(String url, Map<String, String> para) throws IOException {
        HttpClient hc = new DefaultHttpClient();
        if (para != null) {
            List<BasicNameValuePair> paralist = new ArrayList<>();
            for (String key : para.keySet()) {
                paralist.add(new BasicNameValuePair(key, para.get(key)));
            }
            String format = URLEncodedUtils.format(paralist, "utf-8");
            if (StringUtils.isNotBlank(format)) {
                url += "?" + format;
            }
        }
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = hc.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());
        if (StringUtils.isNotBlank(response)) {
            return JSONObject.parseObject(response, Map.class);
        }

        return null;
    }

    public WeChatSessionInfo getOpenIdByRequest(HttpServletRequest request) {
        WeChatSessionInfo sessionInfo = (WeChatSessionInfo) request.getSession(true).getAttribute(WeChatSessionInfo.session_key);
        if (sessionInfo == null) {
            String code = request.getParameter("code");
            if (StringUtils.isBlank(code))
                return null;
            try {
                Map map = doOauth2(code);
                String json = JSONObject.toJSONString(map);
                sessionInfo = JSONObject.parseObject(json, WeChatSessionInfo.class);
                request.getSession(true).setAttribute(WeChatSessionInfo.session_key, sessionInfo);
                return sessionInfo;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return sessionInfo;
        }
    }



}
