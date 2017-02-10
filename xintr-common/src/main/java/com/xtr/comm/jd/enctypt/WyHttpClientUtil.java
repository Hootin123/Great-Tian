package com.xtr.comm.jd.enctypt;



import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 崔永刚
 * @since: 13-08-4 下午2:40
 * @version: 1.0.0
 * HttpClient 通信工具类 ，支持http和https通信
 * <p/>
 * https 通信过程中目前是采取信任所有证书方式通信，存在安全隐患
 * <p/>
 * 通过读取JKS证书容器库的方式可以实现信任库的建立，见方法 trustSslSocketFactory()
 * 其他方式的证书，实现信任库，需要后续考虑
 */

public class WyHttpClientUtil {
    public static final int HTTP_OK = 200;
    
    public static final String CHARSET="UTF-8";

    /**
     * 连接超时时间
     */
    public final static int CONNECTION_TIMEOUT = 5000;
    /**
     * 数据读取等待时间
     */
    public final static int SO_TIMEOUT = 10000;
    // 默认的http 通信接口
    public final static int DEFAULT_HTTP_PORT = 80;
    // 默认的https 通信接口
    public final static int DEFAULT_HTTPS_PORT = 443;


    public static final String CONTENTTYPE = "text/xml; charset=utf-8";

    public static final String KEY_STORE_PATH="D:/keyStore/steven.keystore";


    private static Log log = LogFactory.getLog(WyHttpClientUtil.class);

    /**
     * http GET 方式通信 目前适用于 北京交行
     *
     * @param reqURL
     * @param contentType
     * @return
     */
    public String sendGetRequest(String reqURL, String contentType) {

        log.info("HttpClient get方式调用开始,reqURL:" + reqURL);
        String responseContent = null;
        HttpGet httpGet = new HttpGet(reqURL);
        httpGet.addHeader("Content-Type", contentType);
        httpGet.addHeader("User-Agent", "Mozilla/MSIE");
        HttpClient httpClient = new DefaultHttpClient();

        try {

            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("http", DEFAULT_HTTP_PORT, PlainSocketFactory.getSocketFactory()));
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", DEFAULT_HTTPS_PORT, getTrustSSLSocketFactory()));

            //构建POST请求的表单参数
            HttpResponse response = httpClient.execute(httpGet);
            //取得相应码
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HTTP_OK) {
                log.error("http get通信失败，响应码：[" + responseCode + "]");
                throw new RuntimeException("http通信失败,响应码为{"+ responseCode+"}");
            }
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }

            log.info("银行返回报文： " + responseContent);
            log.info("HttpClient get方式调用结束");
        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
            throw new RuntimeException("请求通信连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
            throw new RuntimeException("请求通信读取超时", ste);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
            throw new RuntimeException("请求通信其他错误", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * 发送post 请求
     *
     * @param reqURL      请求url
     * @param requestXML  发送报文
     * @param contentType
     * @param encoding    报文编码方式
     * @return
     */
    public String sendPostRequest(String reqURL, String requestXML, String contentType, String encoding){
        return sendPostRequest(reqURL,requestXML,contentType,encoding,CONNECTION_TIMEOUT,SO_TIMEOUT);
    }

    /**
     * 发送post 请求
     *
     * @param reqURL      请求url
     * @param requestXML  发送报文
     * @param contentType
     * @param encoding    报文编码方式
     * @param timeout_conn 连接超时时间，单位：毫秒
     * @param timeout_read 读取超时时间，单位：毫秒
     * @return
     */
    public String sendPostRequest(String reqURL, String requestXML, String contentType, String encoding,int timeout_conn,int timeout_read) {

        log.info("HttpClient post方式调用开始,reqURL:" + reqURL);
        String responseContent = null;
        HttpPost httpPost = new HttpPost(reqURL);
        httpPost.addHeader("Content-Type", contentType);
        HttpClient httpClient = new DefaultHttpClient();

        try {

            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout_conn);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout_read);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("http", DEFAULT_HTTP_PORT, PlainSocketFactory.getSocketFactory()));
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", DEFAULT_HTTPS_PORT, getTrustSSLSocketFactory()));

            //构建POST请求的表单参数
            httpPost.setEntity(new StringEntity(requestXML, encoding));
            HttpResponse response = httpClient.execute(httpPost);
            //取得相应码
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HTTP_OK) {
                log.error("http通信失败，响应码：[" + responseCode + "]");
                throw new RuntimeException("http通信失败,响应码为{"+responseCode+"}");
            }
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }

            //log.info("银行返回报文： " + responseContent);
            log.info("HttpClient post方式调用结束");
        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
            throw new RuntimeException("请求通信连接超时", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
            throw new RuntimeException("请求通信读取超时", ste);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
            throw new RuntimeException("请求通信其他错误", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * 此方法默认content_type=text/xml; charset=utf-8
     * encoding=UTF-8
     *
     * @param reqURL     请求地址
     * @param requestXML 发送xml 报文
     * @return
     */
    public String sendPostRequest(String reqURL, String requestXML) {
        return sendPostRequest(reqURL, requestXML, CONTENTTYPE, CHARSET);
    }

    /**
     * @param reqURL      请求地址
     * @param requestXML  发送xml 报文
     * @param contentType 默认content_type=text/xml; charset=utf-8
     * @return
     */
    public String sendPostRequest(String reqURL, String requestXML, String contentType) {
        return sendPostRequest(reqURL, requestXML, contentType, CHARSET);
    }

    public String postUrlWithParams(String url, Map params) {
        return postUrlWithParams(url, params, CHARSET);
    }

    /**
     * 发送post请求 ，带有参数
     *
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public String postUrlWithParams(String url, Map params, String encoding) {
        String encode = CHARSET;
        if (!StringUtils.isEmpty(encoding)) {
            encode = encoding;
        }
        log.info("HttpClient方式调用开始");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        // 添加参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null && params.keySet().size() > 0) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                nvps.add(new BasicNameValuePair((String) entry.getKey(),
                        (String) entry.getValue()));
            }
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, encode));
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(
                    entity.getContent(), encode));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();

        } catch (UnsupportedEncodingException e) {
            log.error("创建通信异常", e);
            throw new RuntimeException("创建通信异常",e);
        } catch (IOException e) {
            log.error("读取流文件异常", e);
            throw new RuntimeException("读取流文件异常",e);
        } catch (Exception e) {
            log.error("通讯未知系统异常", e);
            throw new RuntimeException("通讯未知系统异常",e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    log.error("关闭br异常" + e);
                }
            }
        }

    }
    
    public String postSSLUrlWithParams(String url, Map params) {
    	return postSSLUrlWithParams(url,params,CHARSET,null,null);
    }
    
    public String postSSLUrlWithParams(String url, Map params, String keyStorePath,String password) {
    	return postSSLUrlWithParams(url,params,CHARSET,keyStorePath,password);
    }

    /**
     * 发送https post请求 ，带有参数 
     *
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public String postSSLUrlWithParams(String url, Map params, String encoding, String keyStorePath,String password) {
        String encode = CHARSET;
        if (!StringUtils.isEmpty(encoding)) {
            encode = encoding;
        }
//        if(StringUtils.isEmpty(keyStorePath)){
//        	keyStorePath=KEY_STORE_PATH;
//        }
        log.info("HttpsClient方式调用开始");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        // 添加参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null && params.keySet().size() > 0) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                nvps.add(new BasicNameValuePair((String) entry.getKey(),
                        (String) entry.getValue()));
            }
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
            if(StringUtils.isEmpty(keyStorePath)){
            	httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", DEFAULT_HTTPS_PORT,getTrustSSLSocketFactory()));
            }else{
            	httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", DEFAULT_HTTPS_PORT,getSSLSocketFactory(keyStorePath,password)));
            }
            

            httpost.setEntity(new UrlEncodedFormEntity(nvps, encode));
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(
                    entity.getContent(), encode));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();

        } catch (UnsupportedEncodingException e) {
            log.error("创建通信异常", e);
            throw new RuntimeException("创建通信异常",e);
        } catch (IOException e) {
            log.error("读取流文件异常", e);
            throw new RuntimeException("读取流文件异常",e);
        } catch (Exception e) {
            log.error("通讯未知系统异常", e);
            throw new RuntimeException("通讯未知系统异常",e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    log.error("关闭br异常" + e);
                }
            }
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
            log.error("获取可信任的socktFactory出现异常: " + e.getMessage());
        }
        return SSLSocketFactory.getSocketFactory();
    }

    private SSLSocketFactory getSSLSocketFactory(String keyStorePath,String password){
        SSLSocketFactory socketFactory = null;
        try {
            KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream instream = new FileInputStream(new File(keyStorePath));
            //密匙库的密码
            trustStore.load(instream,password.toCharArray());
            //注册密匙库
            socketFactory = new SSLSocketFactory(trustStore);
            //不校验域名
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyStoreException e) {
            log.error("创建SSLSocketFactory异常 KeyStoreException", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } catch (CertificateException e) {
            log.error("创建SSLSocketFactory异常 CertificateException", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } catch (UnrecoverableKeyException e) {
            log.error("创建SSLSocketFactory异常 UnrecoverableKeyException", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("创建SSLSocketFactory异常 NoSuchAlgorithmException", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } catch (IOException e) {
            log.error("创建SSLSocketFactory异常 IOException", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } catch (KeyManagementException e) {
            log.error("创建SSLSocketFactory异常 KeyManagementException", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } catch (Exception e){
            log.error("创建SSLSocketFactory异常 Exception", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        }
         return socketFactory;
    }

}