package com.xtr.comm.enums;

/**
 * @Author Xuewu
 * @Date 2016/9/13.
 */
public enum ShebaoTypeEnum {

    SHEBAO(1, "社保"),
    GJJ(2, "公积金");

    private int code;
    private String name;

    ShebaoTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ShebaoTypeEnum valueOf(int code) {
        if(code == 1) {
            return SHEBAO;
        }else if(code == 2) {
            return GJJ;
        }
        return null;
    }
}
