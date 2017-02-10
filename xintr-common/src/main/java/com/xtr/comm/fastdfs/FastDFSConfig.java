package com.xtr.comm.fastdfs;

/*
 * @author:张峰
 * FastDFS config 
 * usage:已经不用，配置文件 写在 core/rsos-manager/hxshop-manager 的配置文件中
 */
public class FastDFSConfig {
    public static int connect_timeout = 300;
    public static int network_timeout = 3000;
    public static int http_tracker_http_port = 80;

    public static int http_anti_steal_token = 0;
    public static String http_secret_key = "FastDFS1234567890";
    public static String charset = "UTF-8";
    //可以使用多个服务器，中间用; 隔开
    //tracker_server = 192.168.1.127:22122;192.168.1.103:22122
    public static String tracker_server = "192.168.1.103:22122";

    public static String http_server = "http://192.168.1.103";
    //域名名称
    public static String http_domain = "http://www.img1.hxshop.com";

    //是否使用IP，使用设为1，否则设为0
    public static int http_ip_use = 1;
}
