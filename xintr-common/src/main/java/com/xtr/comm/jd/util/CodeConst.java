package com.xtr.comm.jd.util;

import java.util.HashMap;
import java.util.Map;

public class CodeConst {
    public static final String RETURN_PARAM_NULL = "RETURN_PARAM_NULL";//返回数据为null
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String OUT_TRADE_NO_EXIST = "OUT_TRADE_NO_EXIST";
    public static final String TRADE_NOT_EXIST = "TRADE_NOT_EXIST";
    public static final String SUCCESS = "0000";

    //交易状态
    public static final String TRADE_FINI = "FINI"; //交易成功      成功
    public static final String TRADE_CLOS = "CLOS"; //交易关闭      失败
    public static final String TRADE_WPAR = "WPAR"; //等待支付结果   等待
    public static final String TRADE_BUID = "BUID"; //交易建立       等待
    public static final String TRADE_ACSU = "ACSU"; //已受理         等待

    public static final String TRADE_REFUND = "REFU";//交易退款       成功后失败

    private static Map<String, String> codeMap = new HashMap<String, String>();

    //余额不足错误code
    public static final String codeMap_YEBZ = "ACCOUNT_BALANCE_NOT_ENOUGH";

    static {
        codeMap.put("0000", "成功");
        codeMap.put("EXPARTNER_INFO_UNCORRECT", "传入商户接口信息不正确");
        codeMap.put("ILLEGAL_SIGN", "签名验证出错");
        codeMap.put("ILLEGAL_ARGUMENT", "输入参数有错误");
        codeMap.put("ILLEGAL_AUTHORITY", "权限不正确");
        codeMap.put("CUSTOMER_NOT_EXIST", "提交会员不存在");
        codeMap.put("ILLEGAL_CHARSET", "字符集不合法");
        codeMap.put("ILLEGAL_CLIENT_IP", "客户端IP地址无权访问服务");
        codeMap.put("SYSTEM_ERROR", "系统错误");//（非失败，不能按失败处理）
        codeMap.put("OUT_TRADE_NO_EXIST", "外部交易号已经存在");// （非失败，不能按失败处理）
        codeMap.put("TRADE_NOT_EXIST", "交易不存在");// （如果查询返回此信息，不能以失败处理）
        codeMap.put("ILLEGAL_TRADE_TYPE", "无效交易类型");
        codeMap.put("BUYER_USER_NOT_EXIST", "买家会员不存在");
        codeMap.put("SELLER_USER_NOT_EXIST", "卖家会员不存在");
        codeMap.put("BUYER_SELLER_EQUAL", "买家、卖家是同一帐户 ");
        codeMap.put("USER_STATE_ERROR", "会员状态不正确");
        codeMap.put("COMMISION_ID_NOT_EXIST", "佣金收取帐户不存在");
        codeMap.put("COMMISION_SELLER_DUPLICATE", "收取佣金帐户和卖家是同一帐户");
        codeMap.put("COMMISION_FEE_OUT_OF_RANGE", "佣金金额超出范围");
        codeMap.put("TOTAL_FEE_OUT_OF_RANGE", "交易总金额超出范围");
        codeMap.put("ILLEGAL_AMOUNT_FORMAT", "非法金额格式");
        codeMap.put("ILLEGAL_TRADE_AMMOUT", "交易金额不正确");
        codeMap.put("ILLEGAL_TRADE_CURRENCY", "交易币种不正确");
        codeMap.put("SELF_TIMEOUT_NOT_SUPPORT", "不支持自定义超时");
        codeMap.put("COMMISION_NOT_SUPPORT", "不支持佣金 ");
        codeMap.put("VIRTUAL_NOT_SUPPORT", "不支持虚拟収货方式");
        codeMap.put("PAYMENT_LIMITED", "支付受限");
        codeMap.put("ILLEGAL_BANK_CARD_NO", "卡号不正确");
        codeMap.put("ILLEGAL_BANK_CARD_VALID_PERIOD", "卡有效期不正确");
        codeMap.put("ILLEGAL_ID_CARD_NO", "身份证号码不正确");
        codeMap.put("ILLEGAL_BANK_CARD_NAME", "持卡人姓名不正确");
        codeMap.put("ILLEGAL_BANK_CARD_TYPE", "卡类型不正确 ");
        codeMap.put("REFUND_FAILED", "退款失败");
        codeMap.put("CURRENT_PAY_CANNOT_REVOKE", "当前支付请求状态无法撤销");
        codeMap.put("CURRENT_USER_DIFFERENT_FROM", "当前用户和已登录绑定用户不一致");
        codeMap.put("ILLEGAL_PAY_TYPE", "无效支付类型");
        codeMap.put("USER_STATE_ERROR", "用户状态错误");
        codeMap.put("ACCOUNT_BALANCE_NOT_ENOUGH", "账户余额不足");


        codeMap.put("XTR_PAY_INFO_ERR", "付款信息无效");
        codeMap.put("XTR_BUSS_TYPE_ERR", "未知业务类型");
    }

    /**
     * 判断是否客户原因
     *
     * @param errorCode
     * @return
     */
    public static boolean isCustomerReasons(String errorCode) {
//        codeMap.put("ILLEGAL_BANK_CARD_NO", "卡号不正确");
//        codeMap.put("ILLEGAL_BANK_CARD_VALID_PERIOD", "卡有效期不正确");
//        codeMap.put("ILLEGAL_ID_CARD_NO", "身份证号码不正确");
//        codeMap.put("ILLEGAL_BANK_CARD_NAME", "持卡人姓名不正确");
//        codeMap.put("ILLEGAL_BANK_CARD_TYPE", "卡类型不正确 ");
        return StringUtils.equals(errorCode, "ILLEGAL_BANK_CARD_NO")
                || StringUtils.equals(errorCode, "ILLEGAL_BANK_CARD_VALID_PERIOD")
                || StringUtils.equals(errorCode, "ILLEGAL_ID_CARD_NO")
                || StringUtils.equals(errorCode, "ILLEGAL_BANK_CARD_NAME")
                || StringUtils.equals(errorCode, "ILLEGAL_BANK_CARD_TYPE")
                || StringUtils.equals(errorCode, "OUT_TRADE_NO_EXIST")
                || StringUtils.equals(errorCode, "XTR_PAY_INFO_ERR")
                || StringUtils.equals(errorCode, "XTR_BUSS_TYPE_ERR")
                || StringUtils.equals(errorCode, "")
                || StringUtils.isBlank(errorCode);
    }

    public static String getMsg(String code) {
        return codeMap.get(code);
    }

    public static boolean isContainCode(String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        return codeMap.containsKey(code);

    }

}
