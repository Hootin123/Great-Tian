package com.xtr.wechat.util;

import com.xtr.comm.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.UUID;

/**
 * @Author Xuewu
 * @Date 2016/8/26.
 */
@Component
public class WeChatRedPackUtil {

    @Autowired
    private WeChatConfig weChatConfig;

    /// <summary>
    /// //提交微信支付订单，获取订单ID
    /// </summary>
    /// <param name="data">xml参数字符串</param>
    /// <returns></returns>
    String PayPost(String data)
    {

        String returnData = "";
        String bakString="";
        try
        {
            //指定读取证书格式为PKCS12
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //读取本机存放的PKCS12证书文件
            FileInputStream instream = new FileInputStream(new ClassPathResource("/cert/apiclient_cert.p12").getFile());
            try {
                //指定PKCS12的密码(商户ID)
                keyStore.load(instream, weChatConfig.getShoppayid().toCharArray());
            } finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, weChatConfig.getShoppayid().toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();


            try {

                HttpPost httppost = new HttpPost(weChatConfig.getWxurlString());
                httppost.setHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
                StringEntity uefEntity=new StringEntity(data,"UTF-8");
                httppost.setEntity(uefEntity);

                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    HttpEntity entity = response.getEntity();
                    StringBuffer content=new StringBuffer();

                    if (entity != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                        String tempStr;
                        while ((tempStr = bufferedReader.readLine()) != null) {
                            content.append(tempStr);
                        }
                        //if (content.indexOf("<return_code>") >= 0)
                        //{
                        returnData=content.toString();
                        //}
                        bufferedReader.close();
                    }

                    EntityUtils.consume(entity);
                }
                catch (Exception e2) {
                    returnData="2:"+e2.getMessage();
                }finally {
                    response.close();
                }
            }
            catch (Exception e3) {
                returnData="3:"+e3.getMessage();
            }
            finally {
                httpclient.close();
            }

        } catch (Exception e) {
            returnData=e.getMessage();
        }

        return returnData;
    }

    /**
     * 提交微信红包
     * @param openid：微信用户OPENID
     * @param ordrenumber：红包唯一订单号
     * @param money：红包金额（单位分）
     * @param ip：IP地址
     * @return
     */
    public String PaySetCont(String openid, String ordrenumber, int money, String ip)
    {
        String bakns = UUID.randomUUID().toString().replace("-", "");


        String bakstring = "";
        String bakxml = "<xml>";

        bakxml += "<act_name><![CDATA[薪太软红包]]></act_name>";
        bakstring += "act_name=薪太软红包";
        bakxml += "<client_ip><![CDATA["+ip+"]]></client_ip>";
        bakstring += "&client_ip="+ip;
        bakxml += "<mch_billno><![CDATA["+ordrenumber+"]]></mch_billno>";
        bakstring += "&mch_billno="+ordrenumber;
        bakxml += "<mch_id><![CDATA["+weChatConfig.getShoppayid()+"]]></mch_id>";
        bakstring += "&mch_id="+weChatConfig.getShoppayid();
        bakxml += "<nonce_str><![CDATA["+bakns+"]]></nonce_str>";
        bakstring += "&nonce_str="+bakns;
        bakxml += "<re_openid><![CDATA["+openid+"]]></re_openid>";
        bakstring += "&re_openid="+openid;
        bakxml += "<remark><![CDATA[关注薪太软获取更多红包福利]]></remark>";
        bakstring += "&remark=关注薪太软获取更多红包福利";
        bakxml += "<send_name><![CDATA[薪太软]]></send_name>";
        bakstring += "&send_name=薪太软";
        bakxml += "<total_amount><![CDATA["+money+"]]></total_amount>";
        bakstring += "&total_amount="+money;
        bakxml += "<total_num><![CDATA[1]]></total_num>";
        bakstring += "&total_num=1";
        bakxml += "<wishing><![CDATA[关注薪太软获取更多红包福利]]></wishing>";
        bakstring += "&wishing=关注薪太软获取更多红包福利";
        bakxml += "<wxappid><![CDATA["+weChatConfig.getAppId()+"]]></wxappid>";
        bakstring += "&wxappid="+weChatConfig.getAppId();

        bakstring += "&key=" + weChatConfig.getShoppaykey();


        String baksign = StringUtils.getMD5(bakstring).toUpperCase();
        bakxml += "<sign><![CDATA["+baksign+"]]></sign>";
        bakxml += "</xml>";


         //System.out.println(bakxml);
         String returnsString = PayPost(bakxml);

        return returnsString;
    }



}
