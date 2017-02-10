package com.xtr.comm.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;


import java.util.*;


/**
 * Created by abiao on 2016/7/4.
 */
public class HttpUtils {
    /**
     *
     * @param url 请求的url
     * @param http_par post传递的参数
     * @return Map,
     * key:
     * status:1为成功，0为异常，-1为链接超时
     * response：如果status为1，该字段表示http服务端返回的数据
     */
    public static Map<String,Object> getUrlContentByPost(String url,Map<String,String> http_par){
        int status=0;
        String res="";
        int statusCode = -1;//连接返回状态值
        HttpClient httpclient = new DefaultHttpClient();
        //httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,  3000);//连接时间s
        //httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  3000);//数据传输时间6s
        try {

            HttpPost httppost=new HttpPost(url);
            // Execute HTTP request
            //System.out.println("executing request " + httpget.getURI());
            // 添加参数
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            //http_par.entrySet()
            if (http_par != null && http_par.keySet().size() > 0) {
                Iterator iterator = http_par.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                    nvps.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            //httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(httppost);

            statusCode=response.getStatusLine().getStatusCode();

            if( response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                if (entity != null) {
                    status=1;
                    res= EntityUtils.toString(entity);
                    //System.out.println(res);
                    //res=new String(res.getBytes("gbk"),"utf-8");
                }
            }
//      } catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
            if(statusCode != HttpStatus.SC_OK){
                //res =ApplicationData.conn_out_time;
                status=-1;

            }
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("status", status);
        map.put("response", res);

        return map;
    }
}
