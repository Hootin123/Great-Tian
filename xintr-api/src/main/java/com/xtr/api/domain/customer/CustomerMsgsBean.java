package com.xtr.api.domain.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class CustomerMsgsBean extends BaseObject implements Serializable {
    /**
     *  id,所属表字段为customer_msgs.msg_id
     */
    private Long msgId;

    /**
     *  标题,所属表字段为customer_msgs.msg_title
     */
    private String msgTitle;

    /**
     *  内容,所属表字段为customer_msgs.msg_cont
     */
    private String msgCont;

    /**
     *  信息类型 1活动通知 2操作提醒 3账户变动,所属表字段为customer_msgs.msg_type
     */
    private Integer msgType;

    /**
     *  创建时间,所属表字段为customer_msgs.msg_addtime
     */
    private Date msgAddtime;

    /**
     *  用户id,所属表字段为customer_msgs.msg_customer_id
     */
    private Long msgCustomerId;

    /**
     *  ,所属表字段为customer_msgs.msg_customer_name
     */
    private String msgCustomerName;

    /**
     *  ,所属表字段为customer_msgs.msg_from_customer_id
     */
    private Long msgFromCustomerId;

    /**
     *  ,所属表字段为customer_msgs.msg_from_customer_name
     */
    private String msgFromCustomerName;

    /**
     *  状态：1未读，2已读，0删除,所属表字段为customer_msgs.msg_sign
     */
    private Integer msgSign;

    /**
     *  1收件箱 2发件箱,所属表字段为customer_msgs.msg_class
     */
    private Integer msgClass;

    /**
     * 获取 id 字段:customer_msgs.msg_id
     *
     * @return customer_msgs.msg_id, id
     */
    public Long getMsgId() {
        return msgId;
    }

    /**
     * 设置 id 字段:customer_msgs.msg_id
     *
     * @param msgId customer_msgs.msg_id, id
     */
    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    /**
     * 获取 标题 字段:customer_msgs.msg_title
     *
     * @return customer_msgs.msg_title, 标题
     */
    public String getMsgTitle() {
        return msgTitle;
    }

    /**
     * 设置 标题 字段:customer_msgs.msg_title
     *
     * @param msgTitle customer_msgs.msg_title, 标题
     */
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle == null ? null : msgTitle.trim();
    }

    /**
     * 获取 内容 字段:customer_msgs.msg_cont
     *
     * @return customer_msgs.msg_cont, 内容
     */
    public String getMsgCont() {
        return msgCont;
    }

    /**
     * 设置 内容 字段:customer_msgs.msg_cont
     *
     * @param msgCont customer_msgs.msg_cont, 内容
     */
    public void setMsgCont(String msgCont) {
        this.msgCont = msgCont == null ? null : msgCont.trim();
    }

    /**
     * 获取 信息类型 1活动通知 2操作提醒 3账户变动 字段:customer_msgs.msg_type
     *
     * @return customer_msgs.msg_type, 信息类型 1活动通知 2操作提醒 3账户变动
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * 设置 信息类型 1活动通知 2操作提醒 3账户变动 字段:customer_msgs.msg_type
     *
     * @param msgType customer_msgs.msg_type, 信息类型 1活动通知 2操作提醒 3账户变动
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    /**
     * 获取 创建时间 字段:customer_msgs.msg_addtime
     *
     * @return customer_msgs.msg_addtime, 创建时间
     */
    public Date getMsgAddtime() {
        return msgAddtime;
    }

    /**
     * 设置 创建时间 字段:customer_msgs.msg_addtime
     *
     * @param msgAddtime customer_msgs.msg_addtime, 创建时间
     */
    public void setMsgAddtime(Date msgAddtime) {
        this.msgAddtime = msgAddtime;
    }

    /**
     * 获取 用户id 字段:customer_msgs.msg_customer_id
     *
     * @return customer_msgs.msg_customer_id, 用户id
     */
    public Long getMsgCustomerId() {
        return msgCustomerId;
    }

    /**
     * 设置 用户id 字段:customer_msgs.msg_customer_id
     *
     * @param msgCustomerId customer_msgs.msg_customer_id, 用户id
     */
    public void setMsgCustomerId(Long msgCustomerId) {
        this.msgCustomerId = msgCustomerId;
    }

    /**
     * 获取  字段:customer_msgs.msg_customer_name
     *
     * @return customer_msgs.msg_customer_name, 
     */
    public String getMsgCustomerName() {
        return msgCustomerName;
    }

    /**
     * 设置  字段:customer_msgs.msg_customer_name
     *
     * @param msgCustomerName customer_msgs.msg_customer_name, 
     */
    public void setMsgCustomerName(String msgCustomerName) {
        this.msgCustomerName = msgCustomerName == null ? null : msgCustomerName.trim();
    }

    /**
     * 获取  字段:customer_msgs.msg_from_customer_id
     *
     * @return customer_msgs.msg_from_customer_id, 
     */
    public Long getMsgFromCustomerId() {
        return msgFromCustomerId;
    }

    /**
     * 设置  字段:customer_msgs.msg_from_customer_id
     *
     * @param msgFromCustomerId customer_msgs.msg_from_customer_id, 
     */
    public void setMsgFromCustomerId(Long msgFromCustomerId) {
        this.msgFromCustomerId = msgFromCustomerId;
    }

    /**
     * 获取  字段:customer_msgs.msg_from_customer_name
     *
     * @return customer_msgs.msg_from_customer_name, 
     */
    public String getMsgFromCustomerName() {
        return msgFromCustomerName;
    }

    /**
     * 设置  字段:customer_msgs.msg_from_customer_name
     *
     * @param msgFromCustomerName customer_msgs.msg_from_customer_name, 
     */
    public void setMsgFromCustomerName(String msgFromCustomerName) {
        this.msgFromCustomerName = msgFromCustomerName == null ? null : msgFromCustomerName.trim();
    }

    /**
     * 获取 状态：1未读，2已读，0删除 字段:customer_msgs.msg_sign
     *
     * @return customer_msgs.msg_sign, 状态：1未读，2已读，0删除
     */
    public Integer getMsgSign() {
        return msgSign;
    }

    /**
     * 设置 状态：1未读，2已读，0删除 字段:customer_msgs.msg_sign
     *
     * @param msgSign customer_msgs.msg_sign, 状态：1未读，2已读，0删除
     */
    public void setMsgSign(Integer msgSign) {
        this.msgSign = msgSign;
    }

    /**
     * 获取 1收件箱 2发件箱 字段:customer_msgs.msg_class
     *
     * @return customer_msgs.msg_class, 1收件箱 2发件箱
     */
    public Integer getMsgClass() {
        return msgClass;
    }

    /**
     * 设置 1收件箱 2发件箱 字段:customer_msgs.msg_class
     *
     * @param msgClass customer_msgs.msg_class, 1收件箱 2发件箱
     */
    public void setMsgClass(Integer msgClass) {
        this.msgClass = msgClass;
    }
}