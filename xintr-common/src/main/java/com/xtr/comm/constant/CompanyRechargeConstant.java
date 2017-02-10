package com.xtr.comm.constant;

/**
 * <p>企业充值提现</p>
 *
 * @author 任齐
 * @createTime: 2016/7/13 19:15
 */
public interface CompanyRechargeConstant {

    /**
     * 充值
     */
    int RECHARGE_TYPE = 1;

    /**
     * 提现
     */
    int WITHDRAW_TYPE = 2;

    /**
     * 发工资
     */
    int SEND_SALARY_TYPE = 3;

    /**
     * 缴社保
     */
    int SOCIAL_TYPE = 8;

    /**
     * 快速发工资
     */
    int RAPIDLY_PAYROLL_TYPE = 9;

    //状态
    int COMPANY_RECHARGE_STATE_WAIT = 0;//提交订单
    int COMPANY_RECHARGE_STATE_FAIL = 1;//交易失败
    int COMPANY_RECHARGE_STATE_SUCCESS = 2;//交易成功
    int COMPANY_RECHARGE_STATE_CLOSE = 3;//已关闭

    //支付渠道
    int COMPANY_RECHARGE_STATION_NOTHING = 0;//没有
    int COMPANY_RECHARGE_STATION_LIANLIAN = 1;//连连
    int COMPANY_RECHARGE_STATION_JINGDONG = 2;//京东
    int COMPANY_RECHARGE_STATION_YIBAO = 3;//易宝
    int COMPANY_RECHARGE_STATION_WANGYING = 4;//网银
    int COMPANY_RECHARGE_STATION_YOUE = 5;//账户余额支付
    int COMPANY_RECHARGE_STATION_HUIKUAN = 6;//银行汇款

    //订单名称
    String COMPANY_RECHARGE_ORDERNAME_SALARYRECHARGE = "发工资银行汇款充值订单";
    String COMPANY_RECHARGE_ORDERNAME_RAPIDLYCHARGE = "急速发工资银行汇款充值订单";
    String COMPANY_RECHARGE_ORDERNAME_SHEBAORECHARGE = "社保公积金银行汇款充值订单";

    //充值区分发工资
    String COMPANY_RECHARGE_SALARY_COMPARE = "salaryRecharge";//银行汇款
    String COMPANY_RECHARGE_SALARY_AVIAMOUNT = "aviAmount";//账户余额扣款
}
