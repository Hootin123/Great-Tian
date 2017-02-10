package com.xtr.comm.enums;

/**
 * Created by abiao on 2016/6/23.
 */
public enum ErrorCodeEnum {
    CONFIRMPASSWORD_ERROR("E001","输入密码和确认密码不一致"),
    MEMBER_LOGNAME_USED("E002","该登录账号已经有人使用，请申请其他登录账号"),
    PHONE_NUMBER_ERROR("E003","电话号码错误"),

    MSHORTMESSAGE_ERROR("E004","输入的手机短信验证码不正确，请重新输入"),
    MEMBER_LOGNAME_OR_PASSWORD_ERROR("E005","输入用户名或者密码不正确"),
    MEMBER_LOGNAME_NOT_EXIST("E006","登录账号不存在"),
    MEMBER_LOGNAME_INVALID("E007","登录账号不可用"),
    VERIFYCODE_ERROR("E008","验证码输入错误"),
    PHONE_USED_BY_MEMBER_LOGNAME("E009","已经有账户注册时使用过该手机号"),
    SMS_TEMPLATE_ERROR("E010","短信模板错误"),
    SMS_OVER_LIMIT("E011","该手机号当天注册验证短信条数已用完"),
    SMS_SEND_FAILURE("E012","注册短信验证接受失败"),
    ACTIVECODE_ERROR("E013","激活码错误"),
    REGISTERACTIVE_OVERTIME("E014","注册激活超时"),
    UNACTIVE("E015","该注册账户未被激活"),
    EMAIL_FORMAT_ERROR("E016","邮箱格式错误");

    private final String errorCode;
    private final String message;
    /**
     * 私有构造函数
     *
     * @param errorCode
     * @param message
     */
    private ErrorCodeEnum(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
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
    public static ErrorCodeEnum getByCode(String code) {
        for (ErrorCodeEnum acctErrorCode : values()) {
            if (acctErrorCode.getErrorCode().equals(code)) {
                return acctErrorCode;
            }
        }
        return null;
    }
}
