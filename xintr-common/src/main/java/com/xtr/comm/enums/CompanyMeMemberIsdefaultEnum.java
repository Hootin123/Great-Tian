package com.xtr.comm.enums;

/**
 * Created by abiao on 2016/6/26.
 */
public enum CompanyMeMemberIsdefaultEnum {
    IS_MANAGER(1,"IS_MANAGER","是管理员"),
    NO_MANAGER(0,"NO_MANAGER","不是管理员")
    ;
    private final int code;
    private final String name;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    CompanyMeMemberIsdefaultEnum(int code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }
}
