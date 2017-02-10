package com.xtr.comm.sbt;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.xtr.comm.basic.Config;
import com.xtr.comm.sbt.api.*;
import com.xtr.comm.util.PropUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>社保通接口调用</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 9:04.
 */
public class SheBaoTong {

    private static final Logger LOGGER = LoggerFactory.getLogger(SheBaoTong.class);

    /**
     * 连接超时
     */
    public static final int CONN_TIME_OUT = 10 * 1000;

    /**
     * 读取超时
     */
    public static final int READ_TIME_OUT = 20 * 1000;

    /**
     * 访问token
     */
    private String access_token = null;

    /**
     * 企业在社保通的编码
     */
    private String usercode = null;

    /**
     * Http client
     */
    private HttpClient client;

    /**
     * 接口地址
     */
    private String apiUrl = "http://test.shebaotong.com/";

    /**
     * 设置是否是调试模式，调试模式会以debug输出请求接口和返回数据
     */
    private boolean debug;

    /**
     * 正式环境
     */
    public SheBaoTong(boolean debug){
        Config config = PropUtils.load("classpath:config/common.properties");
        this.apiUrl = config.get("sbt.api_url");
        this.access_token = config.get("sbt.access_token");
        this.usercode = config.get("sbt.usercode");
        this.client = new HttpClient();
        Protocol myhttps = new Protocol("https", new AllowedAllSSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        this.debug = debug;
    }

    /**
     * 测试模式
     * @param accessToken   accessToken
     * @param debug         是否开启debug模式
     */
    public SheBaoTong(String accessToken, String usercode, boolean debug){
        this.access_token = accessToken;
        this.client = new HttpClient();
        this.usercode = usercode;
        this.debug = debug;
    }

    /**
     * 获取可缴纳城市数据
     *
     * @url     http://wiki.shebaotong.com/base/getcities
     * @return
     * @throws IOException
     * @throws HttpException
     * @throws JSONException
     */
    public List<City> getCities() throws IOException, HttpException, JSONException {
        SbtResponse sbtResponse = this.exeute(API_ACTION.BASE_GETCITIES, HttpMethod.POST, "name", "jack");
        return JSON.parseArray(sbtResponse.getData(), City.class);
    }

    /**
     * 按城市获取社保公积金基础数据
     *
     * @url http://wiki.shebaotong.com/base/getbasic
     *
     * @param city      城市编码，对应获取可缴纳城市
     * @return
     * @throws IOException
     * @throws HttpException
     * @throws JSONException
     */
    public Basic getBasic(String city) throws IOException, HttpException {
        SbtResponse sbtResponse = this.exeute(API_ACTION.BASE_GETBASIC, HttpMethod.POST, "city", city);
        return JSON.parseObject(sbtResponse.getData(), Basic.class);
    }

    /**
     * 计算社保公积金各项金额
     *
     * @param insTypes
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public JSONArray calculate(List<InsType> insTypes) throws IOException, HttpException {
        SbtResponse sbtResponse = this.exeute(API_ACTION.BASE_CALCULATE, HttpMethod.POST, "data", JSON.toJSONString(insTypes));
        return JSON.parseArray(sbtResponse.getData());
    }

    /**
     * 计算补缴时社保公积金各项金额
     *
     * @param insTypes
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public JSONArray calcoverdue(List<InsType> insTypes) throws IOException, HttpException {
        SbtResponse sbtResponse = this.exeute(API_ACTION.BASE_CALCOVERDUE, HttpMethod.POST, "data", JSON.toJSONString(insTypes));
        return JSON.parseArray(sbtResponse.getData());
    }

    /**
     * 提交代缴订单
     *
     * @url http://wiki.shebaotong.com/order/order-place
     *
     * @param orderPlace
     * @return
     * @throws IOException
     * @throws HttpException
     * @throws JSONException
     */
    public SbtResponse orderPlace(OrderPlace orderPlace) throws IOException, HttpException {

        List<String> params = new ArrayList<>();
        params.add("usercode");
        params.add(orderPlace.getUsercode());
        if(null != orderPlace.getInvoice()){
            params.add("invoice");
            params.add(orderPlace.getInvoice().toJSONString());
        }
        if(null != orderPlace.getEmps()){
            params.add("emps");
            params.add(orderPlace.getEmps().toJSONString());
        }
        if(null != orderPlace.getAdd()){
            params.add("add");
            params.add(orderPlace.getAdd().toJSONString());
        }
        if(null != orderPlace.getKeep()){
            params.add("keep");
            params.add(orderPlace.getKeep().toJSONString());
        }
        if(null != orderPlace.getStop()){
            params.add("stop");
            params.add(orderPlace.getStop().toJSONString());
        }
        if(null != orderPlace.getOverdue()){
            params.add("overdue");
            params.add(orderPlace.getOverdue().toJSONString());
        }
        if(null != orderPlace.getBasechg()){
            params.add("basechg");
            params.add(orderPlace.getBasechg().toJSONString());
        }

        String[] paramsArr = params.toArray(new String[params.size()]);

        return this.exeute(API_ACTION.ORDER_PLACE, HttpMethod.POST, paramsArr);
    }

    /**
     * 修改订单状态
     *
     * @url         http://wiki.shebaotong.com/order/order-manager
     *
     * @param orderId   订单编号
     * @param status    订单状态
                        0:取消
                        1:已确认
                        2:已付款
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public SbtResponse orderManager(String orderId, int status) throws IOException, HttpException {

        List<String> params = new ArrayList<>();
        params.add("usercode");
        params.add(this.usercode);
        params.add("orderid");
        params.add(orderId);
        params.add("status");
        params.add(status+"");

        String[] paramsArr = params.toArray(new String[params.size()]);

        return this.exeute(API_ACTION.ORDER_MANAGER, HttpMethod.POST, paramsArr);
    }

    /**
     * 提交人员材料
     *
     * @url     http://wiki.shebaotong.com/order/order-additional
     * @param employeeAttach
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public SbtResponse orderAdditional(EmployeeAttach employeeAttach) throws IOException, HttpException {

        List<String> params = new ArrayList<>();
        params.add("usercode");
        params.add(this.usercode);
        params.add("id");
        params.add(employeeAttach.getId());
        params.add("name");
        params.add(employeeAttach.getName());
        if(null != employeeAttach.getAttachment()){
            params.add("attachment");
            params.add(JSON.toJSONString(employeeAttach.getAttachment()));
        }

        String[] paramsArr = params.toArray(new String[params.size()]);

        return this.exeute(API_ACTION.ORDER_ADDITIONAL, HttpMethod.POST, paramsArr);
    }

    public SbtResponse exeute(API_ACTION action, HttpMethod method, String...params) throws IOException, HttpException, JSONException{
        StringBuilder b = generateURL(action);
        NameValuePair[] nameValuePairs = addQueryParameters(params);
        String url = b.toString();
        if(url.endsWith("&") || url.endsWith("?")){
            url = url.substring(0, url.length() - 1);
        }

        return execute(method, url, nameValuePairs);
    }

    private SbtResponse execute(HttpMethod method, String url, NameValuePair[] nameValuePairs) throws IOException, HttpException, JSONException {
        HttpMethodBase httpMethodBase = null;

        if (debug) {
            LOGGER.debug("ApiUrl: {}", url);
        }

        if(method.equals(HttpMethod.GET)){
            httpMethodBase = new GetMethod(url);
            httpMethodBase.setQueryString(nameValuePairs);
        }
        if(method.equals(HttpMethod.POST)){
            PostMethod postMethod = new PostMethod(url);
            postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
            postMethod.setRequestBody(nameValuePairs);
//            httpMethodBase = postMethod;
            // 设置连接超时和读取超时
            HttpParams params = client.getParams();
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONN_TIME_OUT);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIME_OUT);

            int rcode = client.executeMethod(postMethod);
            if (rcode != HttpStatus.SC_OK) {
                throw new HttpException("Non-200 HTTP Status code returned: " + rcode);
            }

            String response = null;
            if (postMethod != null){
                response = postMethod.getResponseBodyAsString();
            }

            if (debug) {
                LOGGER.debug(response);
            }
            return new SbtResponse(response);
        }

        // 设置连接超时和读取超时
        HttpParams params = client.getParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONN_TIME_OUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIME_OUT);

        int rcode = client.executeMethod(httpMethodBase);
        if (rcode != HttpStatus.SC_OK) {
            throw new HttpException("Non-200 HTTP Status code returned: " + rcode);
        }

        String response = null;
        if (httpMethodBase != null){
            response = httpMethodBase.getResponseBodyAsString();
        }

        if (debug) {
            LOGGER.debug(response);
        }
        return new SbtResponse(response);
    }

    /**
     * 生成url
     * @param action
     * @return
     */
    private StringBuilder generateURL(API_ACTION action) {
        return generateURL(action.getActionName());
    }

    /**
     * 生成url
     * @param action
     * @return
     */
    private StringBuilder generateURL(String action) {
        StringBuilder b = new StringBuilder(this.apiUrl + action);
        if (access_token != null) {
            b.append("?access_token=" + access_token);
        } else {
            b.append('?');
        }
        return b;
    }

    /**
     * 添加请求参数
     * @param parameters
     * @return
     */
    private NameValuePair[] addQueryParameters(String... parameters) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (int x = 0, len=parameters.length; x < len; x++) {
            if (x + 1 < parameters.length) {
                //b.append(parameters[x + 1]);
                pairs.add(new NameValuePair(parameters[x], parameters[x + 1]));
            }
            x++;
        }
        return pairs.toArray(new NameValuePair[0]);
    }


    /**
     * 字符串编码
     * @param s
     * @return
     */
    private String encode(String s) {
        String str = s;
        try {
            str = URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            // 忽略
        }
        return str;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * 修改订单状态
     *
     * @url         http://wiki.shebaotong.com/order/order-manager
     *
     * @param orderId   订单编号
     * @param status    订单状态
    0:取消
    1:已确认
    2:已付款
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public SbtResponse orderManager(String orderId, int status,String prove,String suf) throws IOException, HttpException {

        List<String> params = new ArrayList<>();
        params.add("usercode");
        params.add(this.usercode);
        params.add("orderid");
        params.add(orderId);
        params.add("status");
        params.add(status+"");
        params.add("prove");
        params.add(prove+"");
        params.add("suf");
        params.add(suf+"");

        String[] paramsArr = params.toArray(new String[params.size()]);

        return this.exeute(API_ACTION.ORDER_MANAGER, HttpMethod.POST, paramsArr);
    }


    class AllowedAllSSLProtocolSocketFactory implements ProtocolSocketFactory {

        private SSLContext sslcontext = null;

        private SSLContext createSSLContext() {
            SSLContext sslcontext=null;
            try {
                sslcontext = SSLContext.getInstance("SSL");
                sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return sslcontext;
        }

        private SSLContext getSSLContext() {
            if (this.sslcontext == null) {
                this.sslcontext = createSSLContext();
            }
            return this.sslcontext;
        }

        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(
                    socket,
                    host,
                    port,
                    autoClose
            );
        }

        public Socket createSocket(String host, int port) throws IOException,
                UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(
                    host,
                    port
            );
        }


        public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
                throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
        }

        public Socket createSocket(String host, int port, InetAddress localAddress,
                                   int localPort, HttpConnectionParams params) throws IOException,
                UnknownHostException, ConnectTimeoutException {
            if (params == null) {
                throw new IllegalArgumentException("Parameters may not be null");
            }
            int timeout = params.getConnectionTimeout();
            SocketFactory socketfactory = getSSLContext().getSocketFactory();
            if (timeout == 0) {
                return socketfactory.createSocket(host, port, localAddress, localPort);
            } else {
                Socket socket = socketfactory.createSocket();
                SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
                SocketAddress remoteaddr = new InetSocketAddress(host, port);
                socket.bind(localaddr);
                socket.connect(remoteaddr, timeout);
                return socket;
            }
        }

        //自定义私有类
        class TrustAnyTrustManager implements X509TrustManager {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }


    }
}
