package com.xtr.comm.constant;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/12 10:12
 */
public class CompanyConstant {
    /**
     * 企业签约
     */
    //企业协议状态
    public static final int COMPANY_PROTOCOL_YES = 1;//有效
    public static final int COMPANY_PROTOCOL_NO = 2;//无效
    //企业协议类型
    public static final int COMPANY_PROTOCOLTYPE_ACCEPT = 1;//代发
    public static final int COMPANY_PROTOCOLTYPE_PAID = 2;//垫发
    public static final int COMPANY_PROTOCOLTYPE_CANCEL = 0;//取消
    /**
     * 企业用户
     */
    //企业用户状态
    public static final int COMPANYMEMBER_SIGN_NOACTIVE = 2;//未激活
    public static final int COMPANYMEMBER_SIGN_ACTIVE = 3;//已激活
    //邮箱找回密码的验证码编码
    public static final String COMPANYMEMBER_RETRIEVEPWD_CODE = "YXZHMM";//已激活
    //审核状态
    public static final int COMPANYMEMBER_AUDITSTATUS_ING = 0;//待审核
    public static final int COMPANYMEMBER_AUDITSTATUS_BACK = 1;//审核驳回
    public static final int COMPANYMEMBER_AUDITSTATUS_PASS = 2;//审核通过

    //是否有签约的协议
    public static final int COMPANYMEMBER_CONTRACTPROTOCOL_YES = 3;//是
    public static final int COMPANYMEMBER_CONTRACTPROTOCOL_NO = 4;//否
    //是否有访问菜单的权限
    public static final int COMPANYMEMBER_VISITMENU_YES = 1;//是
    public static final int COMPANYMEMBER_VISITMENU_NO = 2;//否
    public static final String COMPANYMEMBER_VISITMENU_VALIDATENAME = "我的账户";//已激活
    public static final String COMPANYMEMBER_VISITMENU_BXMANAGER = "报销管理";//已激活
}
