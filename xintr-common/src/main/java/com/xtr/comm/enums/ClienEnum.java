package com.xtr.comm.enums;

/**
 * <p>终端类型</p>
 *
 * @author 任齐
 * @createTime: 2016/8/3 10:14
 */
public enum ClienEnum {

    WEB("web端", 1),
    WAP("wap端", 1),
    IOS("ios端", 1),
    ANDROID("android端", 1);

    private String msg;
    private int code;

    ClienEnum(String msg, int code){
        this.msg = msg;
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

}
