package com.xtr.customer.controller;

import com.alibaba.fastjson.JSONObject;
import com.xtr.comm.jd.util.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/12.
 */
@Controller
public class SbtCodeController {

    /**
     * 获取社保通USERcode
     *
     * @return
     */
    @RequestMapping("/sbtcode.htm")
    @ResponseBody
    public String sbtUsercode() throws Exception {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getTrustSSLSocketFactory()));

            HttpPost httpost = new HttpPost("https://api.shebaotong.com/v1/base/createaccount");

            ClassPathResource classPathResource = new ClassPathResource("/营业执照.jpg");
            String base64Code = Base64.encode(FileUtils.readFileToByteArray(classPathResource.getFile()));

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            nvps.add(new BasicNameValuePair("access_token", "598956ecedef99f5be806ae4465ece7f"));
            nvps.add(new BasicNameValuePair("name", "薪太软（上海）科技发展有限公司"));
            nvps.add(new BasicNameValuePair("phone", "13062681855"));
            nvps.add(new BasicNameValuePair("email", "anna.wang@xintairuan.com"));
            nvps.add(new BasicNameValuePair("contact", "王惠颖"));

            JSONObject bl = new JSONObject();
            bl.put("suf", "jpg");
            bl.put("data", base64Code);
            nvps.add(new BasicNameValuePair("bl", bl.toJSONString()));

            JSONObject trc = new JSONObject();
            trc.put("suf", "jpg");
            trc.put("data", base64Code);
            nvps.add(new BasicNameValuePair("trc", trc.toJSONString()));

            JSONObject occ = new JSONObject();
            occ.put("suf", "jpg");
            occ.put("data", base64Code);
            nvps.add(new BasicNameValuePair("occ", occ.toJSONString()));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            HttpResponse httpResponse = client.execute(httpost);
            HttpEntity entity = httpResponse.getEntity();

            return EntityUtils.toString(entity);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public SSLSocketFactory getTrustSSLSocketFactory() {
        SSLSocketFactory socketFactory = null;
        try {
            socketFactory = new SSLSocketFactory(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }, new AllowAllHostnameVerifier());
            return socketFactory;
        } catch (Exception e) {
            System.out.println("获取可信任的socktFactory出现异常: " + e.getMessage());
        }
        return SSLSocketFactory.getSocketFactory();
    }
}
