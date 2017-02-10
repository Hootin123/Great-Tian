package com.xtr.comm.fastdfs;

import java.io.Serializable;


/**
 * <p>FastDFSResult, 执行FastDFS的结果</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/16 16:02
 */
public class FastDFSResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8913090363752521357L;
    public boolean is_success;   //是否执行成功
    public String error_info;    //错误信息
    public String remote_file_url;  //远程文件的链接地址,不包括域名或IP，只包含后面的部分
    public String group_name;//文件分组
    public String remote_filename;

//    public int img_width;   //图片宽
//    public int img_height;  //图片高
}
