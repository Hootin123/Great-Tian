package com.xtr.api.basic;

import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import java.io.Serializable;

/**
 * <p>App接口返回响应对象</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/8 13:41
 */
public class AppResponse implements Serializable{

    /**
     * 是否成功
     */
    private boolean isSuccess = false;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 口令
     */
    private String toKen;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 数据对象
     */
    private Object data;

    /**
     * 分页
     */

    private Paginator paginator;

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToKen() {
        return toKen;
    }

    public void setToKen(String toKen) {
        this.toKen = toKen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
