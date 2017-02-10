package com.xtr.core.service.send;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.comm.util.PropertyUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * <p>短信發送实现类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/11 9:29
 */
@Service("sendMsgService")
public class SendMsgServiceImpl implements SendMsgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMsgServiceImpl.class);

    static boolean IS_SEND = PropertyUtils.getBool("xtr.issend");

    /**
     * 短信发送
     *
     * @param mobile
     * @param content
     * @return
     */
    public SendMsgResponse sendMsg(String mobile, String content) throws IOException {
        SendMsgResponse sendMsgResponse = new SendMsgResponse();
        sendMsgResponse.setMobile(mobile);

        if(IS_SEND){
            HttpClient httpclient = new HttpClient();
            PostMethod methodpost = new PostMethod(PropertyUtils.getString("send.msg.url"));
            httpclient.getParams().setContentCharset("UTF-8");
            methodpost.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
            methodpost.setRequestHeader("Accept-Charset", "utf-8");
            methodpost.setRequestHeader("Connection", "close");
            methodpost.addParameter("username", PropertyUtils.getString("send.msg.auth.name"));
            methodpost.addParameter("userpwd", PropertyUtils.getString("send.msg.auth.pwd"));
            methodpost.addParameter("usersource", "4");
            methodpost.addParameter("cont", content);
            methodpost.addParameter("usermobile", mobile);
            methodpost.releaseConnection();
            try {
                int state = httpclient.executeMethod(methodpost);
                if (state == 200) {
                    String responses = methodpost.getResponseBodyAsString();
                    String returnString = URLDecoder.decode(responses, "utf-8");
                    JSONArray jsonArray = JSONArray.parseArray(returnString);
                    JSONObject json = jsonArray.getJSONObject(0);
                    Integer jsonstart = json.getInteger("start");
                    Integer jsonsign = json.getInteger("sign");
                    String jsonstring = json.getString("datastring");
                    sendMsgResponse.setStart(jsonstart);
                    sendMsgResponse.setSign(jsonsign);
                    if (jsonstart == 1 && jsonsign == 1 && jsonstring.equals("1")) {
                        sendMsgResponse.setSuccess(true);
                    } else {
                        sendMsgResponse.setError(json.getString("error"));
                        sendMsgResponse.setLog(json.getString("log"));
                    }
                } else {
                    sendMsgResponse.setError("网络错误，连接失败。" + String.valueOf(state));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                sendMsgResponse.setError("短信发送失败");
            }
        } else{
            sendMsgResponse.setSuccess(true);
        }
        LOGGER.info(JSON.toJSONString(sendMsgResponse));
        return sendMsgResponse;
    }
}
