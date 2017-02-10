package com.xtr.comm.enums;

/**
 * Created by abiao on 2016/6/23.
 */
public enum CompanyMeMemberSignEnum {
    AVAILABLE(1,"AVAILABLE","可用"),
    UNAVAILABLE(0,"UNAVAILABLE","不可用"),
    ACTIVE(3,"ACTIVE","已激活"),
    UNACTIVE(2,"UNACTIVE","未激活"),
    UNACTIVE_AND_OVERTIME(8,"UNACTIVE_AND_OVERTIME","未激活并超时"),
    UNREGISTER(9,"UNREGISTER","未注册");



    private final int code;
    private final String name;
    private final String message;
    /**
     * 私有构造函数
     *
     * @param code
     * @param message
     */
    private CompanyMeMemberSignEnum(int code,String name, String message) {
        this.name = name;
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;

    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return
     */
    public static CompanyMeMemberSignEnum getByCode(int code) {
        for (CompanyMeMemberSignEnum acctcode : values()) {
            if (acctcode.getCode()==code) {
                return acctcode;
            }
        }
        return null;
    }

}
