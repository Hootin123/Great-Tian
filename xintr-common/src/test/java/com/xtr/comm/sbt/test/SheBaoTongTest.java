package com.xtr.comm.sbt.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtr.comm.basic.Config;
import com.xtr.comm.jd.util.Base64;
import com.xtr.comm.sbt.SbtResponse;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.*;
import com.xtr.comm.util.PropUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>社保通接口测试</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 9:15.
 */
public class SheBaoTongTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SheBaoTongTest.class);

    private SheBaoTong sheBaoTong;

    @Before
    public void before() {
        Config config = PropUtils.load("classpath:config/common.properties");
        String access_token = config.get("sbt.access_token");
        String usercode = config.get("sbt.usercode");
        sheBaoTong = new SheBaoTong(access_token, usercode, true);
    }

    /**
     * 测试获取可缴纳城市
     * @throws Exception
     */
    @Test
    public void testgetCities() throws Exception {
        List<City> cities = sheBaoTong.getCities();
        LOGGER.info("data\t: {}", JSON.toJSONString(cities));
    }

    /**
     * 按城市获取社保公积金基础数据
     */
    @Test
    public void testgetBasic() throws Exception{
        Basic basic = sheBaoTong.getBasic("shanghai");
        LOGGER.info("data\t: {}", basic.toJSONString());
    }

    /**
     * 计算社保公积金各项金额
     */
    @Test
    public void testCalculate() throws Exception{
        List<InsType> insTypes = new ArrayList<>();
//        insTypes.add(new InsType("shanghaigongjijin", 5000));
        insTypes.add(new InsType("shanghaiwuxian", 5000));
        JSONArray results = sheBaoTong.calculate(insTypes);
        LOGGER.info("results\t: {}", JSON.toJSONString(results));
    }

    /**
     * 计算补缴时社保公积金各项金额
     */
    @Test
    public void testCalcoverdue() throws Exception{
        List<InsType> insTypes = new ArrayList<>();
//        insTypes.add(new InsType("shanghaigongjijin", 5000, "201608"));
//        insTypes.add(new InsType("shanghaisanxian", 5000, "201608"));
//        insTypes.add(new InsType("shanghaiwuxian", 5000, "201608"));
        JSONArray results = sheBaoTong.calcoverdue(insTypes);
        LOGGER.info("results\t: {}", JSON.toJSONString(results));
    }

    /**
     * 提交代缴订单
     */
    @Test
    public void testOrderPlace() throws Exception{
        OrderPlace orderPlace = new OrderPlace();
        orderPlace.setUsercode("20160809896");

//        JSONArray emps = new JSONArray();
//        JSONObject emp = new JSONObject();
//        emp.put("id", "330726196507040016");
//        emp.put("name", "黄晓明");
//        emp.put("idtype", "idcard");
//        emp.put("phone", "15900965678");
//        emps.add(emp);
//
//        orderPlace.setEmps(emps);

//        JSONArray add = new JSONArray();
//        JSONObject rq = new JSONObject();
//        rq.put("id", "330726196507040016");
//        rq.put("name", "黄晓明");
//        rq.put("ins", new InsType("shanghaisanxian", 999999999));
//        rq.put("hf", new InsType("shanghaigongjijin", 5000));
//        add.add(rq);
//        orderPlace.setAdd(add);

        // 汇缴
//        JSONArray keep = new JSONArray();
//        JSONObject hj = new JSONObject();
//        hj.put("id", "610428199301095515");
//        hj.put("name", "任齐");
//        hj.put("ins", new InsType("shanghaisanxian", 999999999));
//        hj.put("hf", new InsType("shanghaigongjijin", 5000));
//        keep.add(hj);
//
//        orderPlace.setKeep(keep);


        JSONArray overdue = new JSONArray();

        JSONArray sb1 = new JSONArray();

        JSONObject b1 = new JSONObject();
        b1.put("month", "201607");
        b1.put("base", 4000);
        sb1.add(b1);

        JSONObject b2 = new JSONObject();
        b2.put("month", "201608");
        b2.put("base", 9999999);
        sb1.add(b2);

        JSONObject b3 = new JSONObject();
        b3.put("month", "201608");
        b3.put("base", 4000);
        sb1.add(b3);

        JSONObject bj1 = new JSONObject();
        bj1.put("id", "330726196507040016");
        bj1.put("name", "黄晓明");
        bj1.put("ins", sb1);
        bj1.put("hf", sb1);
        overdue.add(bj1);
        /*
        JSONObject bj2 = new JSONObject();
        bj2.put("id", "330726196507040016");
        bj2.put("name", "黄晓明");
        bj2.put("ins", b2);
        bj2.put("hf", b2);
        overdue.add(bj2);

        JSONObject bj3 = new JSONObject();
        bj3.put("id", "330726196507040016");
        bj3.put("name", "黄晓明");
        bj3.put("ins", new InsType("shanghaisanxian", 4000));
        bj3.put("hf", new InsType("shanghaigongjijin", 5000));
        overdue.add(bj3);*/

        orderPlace.setOverdue(overdue);

        System.out.println(JSON.toJSONString(orderPlace));

        SbtResponse sbtResponse = sheBaoTong.orderPlace(orderPlace);
        System.out.println(sbtResponse.getData());
    }

    /**
     * 修改订单状态
     */
    @Test
    public void testOrderManager() throws Exception{

        String orderId = "";
        int status = 1;

        SbtResponse sbtResponse = sheBaoTong.orderManager(orderId, status);
        System.out.println(sbtResponse.getData());
    }

    /**
     * 提交人员材料
     * @throws Exception
     */
    @Test
    public void testOrderAdditional() throws Exception{
//卜雪   331127197606283338      朱从筠  370303198810319386
        EmployeeAttach employeeAttach = new EmployeeAttach();
        employeeAttach.setId("370303198810319386");
        employeeAttach.setName("朱从筠");

        File file = new File("d:/aa.jpg");

        String base64Code = Base64.encode(FileUtils.readFileToByteArray(file));

        System.out.println(base64Code);
        Attachment attachment = new Attachment(base64Code);
        employeeAttach.setAttachment(attachment);

        SbtResponse sbtResponse = sheBaoTong.orderAdditional(employeeAttach);
        System.out.println(sbtResponse.getData());
    }

    @Test
    public void testCreateZS() throws Exception{
        DefaultHttpClient client = new DefaultHttpClient();
        client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, getTrustSSLSocketFactory()));

        HttpPost httpost = new HttpPost("https://api.shebaotong.com/v1/base/createaccount");

        File file = new File("d:/营业执照.jpg");
        String base64Code = Base64.encode(FileUtils.readFileToByteArray(file));

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

        System.out.println(EntityUtils.toString(entity));

        httpost.abort();
    }

    @Test
    public void testHttps() throws Exception{
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", 8443, sslSocketFactory));

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
            LOGGER.error("获取可信任的socktFactory出现异常: " + e.getMessage());
        }
        return SSLSocketFactory.getSocketFactory();
    }

    @Test
    public void testCreateCS() throws Exception{
        File file = new File("d:/aa.jpg");
        String base64Code = "123";

        JSONObject json = new JSONObject();
        json.put("access_token", "fdsf");
        json.put("name", "fs（fds）fds");
        json.put("phone", "fds");
        json.put("email", "fds.fds@fds.com");
        json.put("contact", "fds");

        JSONObject bl = new JSONObject();
        bl.put("suf", "jpg");
        bl.put("data", base64Code);
        json.put("bl", bl);

        JSONObject trc = new JSONObject();
        trc.put("suf", "jpg");
        trc.put("data", base64Code);
        json.put("trc", trc);

        JSONObject occ = new JSONObject();
        occ.put("suf", "jpg");
        occ.put("data", base64Code);
        json.put("occ", occ);

        System.out.println(json);

        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod("http://test.shebaotong.com/base/createaccount");
        postMethod.setRequestEntity(new StringRequestEntity(json.toJSONString(), "application/json", "utf-8"));

        int rcode = client.executeMethod(postMethod);
        System.out.println("rcode" + rcode);

        String response = null;
        if (postMethod != null){
            response = postMethod.getResponseBodyAsString();
        }

        System.out.println(response);

        postMethod.abort();
    }
}