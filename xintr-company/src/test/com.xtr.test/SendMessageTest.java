package com.xtr.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.net.URLDecoder;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/11 9:01
 */

public class SendMessageTest {

    public static void main(String[] args) {
        new SendMessageTest().sendSms("hehe", "15857134834");
    }

    private String sendSms(String cont, String mobile) {
        try {
            String authurl = "http://120.26.46.166:8080/lcjAuth/axAuth";
            String authname = "laicaijie";
            String authpwd = "15ec96046f8d019f012416ff7ad775b2";
            String bakString = "1";
            HttpClient httpclient = new HttpClient();

            PostMethod methodpost = new PostMethod(authurl + "/rzSendSmsXbk");

            httpclient.getParams().setContentCharset("UTF-8");
            methodpost.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
            methodpost.setRequestHeader("Accept-Charset", "utf-8");
            methodpost.setRequestHeader("Connection", "close");
            methodpost.addParameter("username", authname);
            methodpost.addParameter("userpwd", authpwd);
            methodpost.addParameter("usersource", "4");
            methodpost.addParameter("cont", cont);
            methodpost.addParameter("usermobile", mobile);
            methodpost.releaseConnection();
            int state = httpclient.executeMethod(methodpost);
            if (state == 200) {
                String responses = methodpost.getResponseBodyAsString();
                String returnString = URLDecoder.decode(responses, "utf-8");
                JSONArray jsonArray = JSONArray.parseArray(returnString);
                JSONObject json = jsonArray.getJSONObject(0);
                Integer jsonstart = json.getInteger("start");
                Integer jsonsign = json.getInteger("sign");
                String jsonstring = json.getString("datastring");
                if (jsonstart == 1 && jsonsign == 1 && jsonstring.equals("1")) {
                    bakString = "1";
                } else {
                    bakString = json.getString("error");
                }
            } else {
                bakString = "网络错误，连接失败。" + String.valueOf(state);
            }
            return bakString;
        } catch (Exception e) {
            return "网络错误，发送失败。";
        }
    }
}
