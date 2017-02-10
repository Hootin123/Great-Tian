package com.xtr.api.basic;

import java.io.Serializable;

/**
 * <p>短信发送响应实体类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/11 9:20
 */

public class SendMsgResponse implements Serializable {

    /**
     * 是否成功
     */
    private boolean isSuccess = false;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 是否连接陈宫
     */
    private int start;

    /**
     * 是否操作成功
     */
    private int sign;

    /**
     * 返回错误提示内容，用于提示用户
     */
    private String error;

    /**
     * 返回错误具体内容，用于调试
     */
    private String log;


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
