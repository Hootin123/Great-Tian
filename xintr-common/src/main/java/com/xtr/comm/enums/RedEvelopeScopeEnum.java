package com.xtr.comm.enums;

/**
 * <p>红包使用范围</p>
 *
 * @author 任齐
 * @createTime: 2016/8/3 17:54
 */
public enum  RedEvelopeScopeEnum {

    SOCIAL("社保", 8)
    ;

    private String type;

    private int code;

    RedEvelopeScopeEnum(String type, int code){
        this.type = type;
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

}
