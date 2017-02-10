package com.xtr.comm.enums;

/**
 * Created by abiao on 2016/7/4.
 */
public enum StationSmsRecordsTypeEnum {
    USER(1,"user","一般用户"),
    MANAGER(2,"manager","管理员");
    private final int code;
    private final String name;
    private final String message;
    /**
     * 私有构造函数
     *
     * @param code
     * @param message
     */
    private StationSmsRecordsTypeEnum(int code,String name, String message) {
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
    public static StationSmsRecordsTypeEnum getByCode(int code) {
        for (StationSmsRecordsTypeEnum acctcode : values()) {
            if (acctcode.getCode()==code) {
                return acctcode;
            }
        }
        return null;
    }

}
