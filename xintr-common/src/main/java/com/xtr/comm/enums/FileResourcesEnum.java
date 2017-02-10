package com.xtr.comm.enums;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/2 14:53
 */
public enum FileResourcesEnum {
    COMPANYSYNOPSIS(1,"COMPANYSYNOPSIS","公司简介"),
    COMPANYVOUCHER(2,"COMPANYVOUCHER","公司相关证件"),
    COMPANYLEGAL(3,"COMPANYLEGAL","法人、股东资料"),
    COMPANYCOPYPAPER(4,"COMPANYCOPYPAPER","签字样本"),
    COMPANYCRDITREPORT(5,"COMPANYCRDITREPORT","贷款卡及企业信用报告"),
    COMPANYREPORTS(6,"COMPANYREPORTS","报表"),
    COMPANYCONTRACT(7,"COMPANYCONTRACT","近期合同"),
    COMPANYACCOUNTS(8,"COMPANYACCOUNTS","账目"),
    COMPANYPAYMENT(9,"COMPANYPAYMENT","缴纳表"),
    COMPANYSPENDING(10,"COMPANYSPENDING","日常开销"),
    COMPANYFINANCE(11,"COMPANYFINANCE","融资明细"),
    COMPANYBANKSWIFT(12,"COMPANYBANKSWIFT","银行对公流水"),
    COMPANYSELFSWIFT(13,"COMPANYSELFSWIFT","个人流水");

    private final int code;
    private final String name;
    private final String message;

    FileResourcesEnum(int code, String name, String message) {
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

//    public static FileResourcesEnum getByCode(int code) {
//        for (FileResourcesEnum acctcode : values()) {
//            if (acctcode.getCode()==code) {
//                return acctcode;
//            }
//        }
//        return null;
//    }
}
