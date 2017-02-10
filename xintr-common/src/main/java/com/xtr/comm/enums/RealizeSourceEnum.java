package com.xtr.comm.enums;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/5 18:18
 */
public enum RealizeSourceEnum {
    BAIDU360(1,"BAIDU360","百度、360等搜索引擎"),
    WEIXINQQ(2,"WEIXINQQ","微信、QQ等社交平吧"),
    MARKET(3,"MARKET","推销"),
    ADVERTISE(4,"ADVERTISE","广告"),
    INTRODUCE(5,"INTRODUCE","朋友介绍"),
    OTHERS(6,"OTHERS","其他");

    private final int code;
    private final String name;
    private final String message;

    RealizeSourceEnum(int code, String name, String message) {
        this.name = name;
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public int getCode() {return code; }

    public String getMessage() {
        return message;
    }
}
