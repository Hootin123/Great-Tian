package com.xtr.comm.enums;

/**
 * <p>业务类型枚举</p>
 *
 * @author 任齐
 * @createTime: 2016/8/3 10:10
 */
public enum BusinessEnum {

    COMPANY_RECHARGE("企业充值", "01"),
    COMPANY_WITHDRAWALS("企业提现", "02"),
    SEND_SALARY("企业发工资", "03"),
    PERSONAL_WITHDRAWALS("个人提现", "04"),
    COMPANY_ADVACE_BORROW("企业垫付借款", "05"),
    COMPANY_FINANCE_BORROW("企业融资借款", "06"),
    COMPANY_PAYROLL_BORROW("企业代发工资借款", "07"),
    COMPANY_WECHAT_FIVE_REDS("微信5元注册红包","08"),
    COMPANY_WECHAT_ADUIT_REDS("微信20元审核红包","09"),
    COMPANY_SHEBAO_ORDER("企业社保订单","10")
    ;

    private String type;

    private String code;

    BusinessEnum(String type, String code){
        this.type = type;
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public String getType() {
        return type;
    }
}
