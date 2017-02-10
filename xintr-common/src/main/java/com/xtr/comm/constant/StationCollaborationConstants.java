package com.xtr.comm.constant;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/26 13:54
 */
public class StationCollaborationConstants {

    //合作意向类型
    public static final int STATIONCOLLABRATION_TYPE_WORKER = 1;//商务合作
    public static final int STATIONCOLLABRATION_TYPE_NEWUSER = 2;//新用户注册
    public static final int STATIONCOLLABRATION_TYPE_COMMITSARLY = 3;//提交工资单
    public static final int STATIONCOLLABRATION_TYPE_BEFORETHIRTY = 4;//签约到期前30天
    public static final int STATIONCOLLABRATION_TYPE_INTENT = 5;//融资意向
    public static final int STATIONCOLLABRATION_TYPE_SURE = 6;//明确签约类型

    //合作意向状态
    public static final int STATIONCOLLABRATION_STATE_WILL = 1;//待联系（默认状态）
    public static final int STATIONCOLLABRATION_STATE_ALREADY = 2;//已联系(暂存)
    public static final int STATIONCOLLABRATION_STATE_FINISH = 3;//已签约
    public static final int STATIONCOLLABRATION_STATE_CLOSE = 4;//关闭

    //手机短信发送模板
    public static final int STATIONSMSCONT_TYPE_FIRST = 1;//类型1
    //手机短信消息发送人类型
    public static final int STATIONRECORDS_USERTYPE_USER = 1;//用户
    public static final int STATIONRECORDS_USERTYPE_MANAGER = 2;//企业管理员
}
