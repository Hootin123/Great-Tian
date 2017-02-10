package com.xtr.api.basic;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import java.io.Serializable;

/**
 * <p>服务响应实体对象</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/13 14:15
 */

public class ResultResponse implements Serializable {

    /**
     * 是否成功
     */
    private boolean isSuccess = false;

    /**
     * 备注
     */
    private String comment;

    /**
     * 消息code
     */
    private String msgCode;

    /**
     * 消息内容
     */
    private String message;

    /**
     * jsonpCallBack值。用于jsonp请求。
     * 如果jsonpCallBack不为空，会自动封装成jsonp格式，
     * 否则依然还是一般json格式数据
     */
    String jsonpCallBack;

    /**
     * 数据对象
     */
    private Object data;

    /**
     * 分页
     */
    private Paginator paginator;

//    /**
//     * 每页大小
//     */
//    private int pageSize;
//
//    /**
//     * 当前页
//     */
//    private int pageIndex;
//
//    /**
//     * 总页数
//     */
//    private int pageTotal;
//
//    /**
//     * 数据总条数
//     */
//    private int totalCount;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getJsonpCallBack() {
        return jsonpCallBack;
    }

    public void setJsonpCallBack(String jsonpCallBack) {
        this.jsonpCallBack = jsonpCallBack;
    }

//    public int getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    public int getPageIndex() {
//        return pageIndex;
//    }
//
//    public void setPageIndex(int pageIndex) {
//        this.pageIndex = pageIndex;
//    }
//
//    public int getPageTotal() {
//        return pageTotal;
//    }
//
//    public void setPageTotal(int pageTotal) {
//        this.pageTotal = pageTotal;
//    }
//
//    public int getTotalCount() {
//        return totalCount;
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//    }


    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public static<T> ResultResponse buildSuccess(T data) {
        ResultResponse rr = new ResultResponse();
        rr.setSuccess(true);
        rr.setData(data);
        return rr;
    }

    public static<T> ResultResponse buildFail(T data) {
        ResultResponse rr = new ResultResponse();
        rr.setSuccess(false);
        rr.setData(data);
        return rr;
    }

    public ResultResponse message(String message){
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
