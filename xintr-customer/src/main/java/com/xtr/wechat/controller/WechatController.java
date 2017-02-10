package com.xtr.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.xtr.wechat.util.WeChatSessionInfo;
import com.xtr.wechat.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Author Xuewu
 * @Date 2016/8/25.
 */
@Controller
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WeChatUtil weChatUtil;

    //获取access_token
    //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx5cf94567a1553afe&secret=77e63ee0a82bb3ee9e8c491bb68008e8
//{"access_token":"HxJksnvQeCvxLQ32hldhd7xd49WZmOYHNDRvaXyY8HIQcRGJyC6Ijpf3XeF-hSb-68PLhNpSbPadhwsfYAOYGlGQqThptOht5x4iYPTueJyprjy53xqABHRSzEBz30icPBWdAEABNI","expires_in":7200}
    @RequestMapping("/checkSignature.htm")
    @ResponseBody
    private void checkSignature(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        String tempStr = weChatUtil.checkSignature(timestamp, nonce);

        if(signature.equals(tempStr)){
            response.getWriter().print(echostr);
        }else{
            response.getWriter().print(tempStr);
        }
    }

    @RequestMapping("/qhb.htm")
    @ResponseBody
    private String qhb(String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);
        if(openIdByRequest == null)
            return "请到微信公众号点击领取红包！";


        Map map = weChatUtil.userInfo(openIdByRequest.getOpenid());

        return "抢红包页面 openId:"+openIdByRequest.getOpenid() + "userInfo:"+ JSONObject.toJSONString(map);
    }



    public static void main(String[] args) {
        System.out.println(URLEncoder.encode("http://wap.xintairuan.com/xintr-customer/h5Hongbao/toHongbaoRegisterPage.htm"));
    }
}
