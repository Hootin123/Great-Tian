package com.xtr.api.dto.gateway.base;

/**
 * Created by xuewu on 2016/8/5.
 */
public enum BusinessType {

    COMPANY_WITHDRAW(1), QUERY_ACCOUNT(2), CUSTOMER_WITHDRAW(3), VALIDATE_CUSTOMER(4), RAPIDLY_WITHDRAW(5);

    private int code;

    public static BusinessType valueOf(int code) {
        if(code == 1) {
            return COMPANY_WITHDRAW;
        }else if(code == 2) {
            return QUERY_ACCOUNT;
        }else if(code == 3){
            return CUSTOMER_WITHDRAW;
        }else if(code == 4) {
            return VALIDATE_CUSTOMER;
        }else if(code == 5) {
            return RAPIDLY_WITHDRAW;
        }
        return null;
    }

    BusinessType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
