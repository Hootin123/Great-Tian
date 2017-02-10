package com.xtr.comm.constant;

/**
 * @Author Xuewu
 * @Date 2016/9/19.
 */
public class ShebaoConstants {
    public static class OrderType {
        //订单类型(1增员，2汇缴，3调基，4,停缴, 5补缴)
        public static final int ZY = 1;
        public static final int HJ = 2;
        public static final int TJ = 3;
        public static final int STOP = 4;
        public static final int BJ = 5;
    }
    //工资单是否扣除状态（0未扣除，1已扣除）
    public static final int PAYROLL_STATUS_YES=1;
    public static final int PAYROLL_STATUS_NO=0;

    //订单状态(0新建)
    public static final int ORDER_STATUS_NEW=0;

    //社保通订单状态(0初始化)
    public static final int SBT_ORDERSTATUS_INIT=0;

    //关联的类型 1社保 2公积金 3基本信息
    public static final int RELATION_TYPE_SHEBAO=1;
    public static final int RELATION_TYPE_GONGJIJING=2;
    public static final int RELATION_TYPE_BASE=3;

    //社保通状态(1未交，2缴纳中，3停缴，4审核失败)
    public static final int SBT_STATE_NO=1;
    public static final int SBT_STATE_ING=2;
    public static final int SBT_STATE_STOP=3;
    public static final int SBT_STATE_FAIL=4;

    //是否缴纳社保或公积金（0否，1是)
    public static final int KEEP_YES=1;
    public static final int KEEP_NO=0;

    //是否当前账单(0否 1是)
    public static final int CURRENT_ORDER_YES=1;
    public static final int CURRENT_ORDER_NO=0;

    //企业订单状态(0初始化，1待提交 2待付款 3付款中 4客户付款成功 5办理中 6办理完结 7订单关闭 8供应商付款成功)
    public static final int COMPANY_ORDER_INIT=0;
    public static final int COMPANY_ORDER_WILLSUBMIT=1;
    public static final int COMPANY_ORDER_WILLPAY=2;
    public static final int COMPANY_ORDER_PAYING=3;
    public static final int COMPANY_ORDER_PAYSUCCESS=4;
    public static final int COMPANY_ORDER_OPERATION=5;
    public static final int COMPANY_ORDER_OPERATIONSUCESS=6;
    public static final int COMPANY_ORDER_CLOSE=7;
    public static final int COMPANY_ORDER_SUPPLYSUCCESS=8;

    //员工订单社保通状态(0初始化 1成功 2失败 3提交账单异常)
    public static final int CUSTOMER_ORDER_SBTSTATE_INIT=0;
    public static final int CUSTOMER_ORDER_SBTSTATE_SUCCESS=1;
    public static final int CUSTOMER_ORDER_SBTSTATE_FAIL=2;
    public static final int CUSTOMER_ORDER_SBTSTATE_SUBMITEXCEPTION=3;

    //是否补退到员工账户 1 是 2 否
    public static final int SUPPLEMENT_BACK_YES=1;
    public static final int SUPPLEMENT_BACK_NO=2;
}
