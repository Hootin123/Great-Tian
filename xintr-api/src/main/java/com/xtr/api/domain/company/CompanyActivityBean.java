package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 公司参与的活动
 * @author zhangshuai
 * @date 2016/8/31.
 */
public class CompanyActivityBean extends BaseObject implements Serializable {

    /**活动id**/
    private Integer activityId;
    /**活动名称***/
    private String activityName;
    /**公司用户登录id***/
    private Long activityMemberId;
    /**参加活动公司id***/
    private Long activityCompanyId;
    /**红包是否已领取  0未领取  1已领取***/
    private Integer activityHasReceive;
    /**活动创建时间***/
    private Date activityCreateTime;
    /**活动红包的种类  1为5元注册红包  2为20元完善信息红包***/
    private  Integer activityReceive;
    /**活动编号***/
    private String activityNumber;
    /**活动订单号***/
    private String  activityOrderNumber;
    /**领取红包的ip地址**/
    private String activityIp;
    /**微信发红包返回结果**/
    private String wechatReturnMsg;

    public String getWechatReturnMsg() {
        return wechatReturnMsg;
    }

    public void setWechatReturnMsg(String wechatReturnMsg) {
        this.wechatReturnMsg = wechatReturnMsg;
    }

    public Integer getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(Integer receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    /**红包金额**/

    private Integer receiveAccount;



    public String getActivityOrderNumber() {
        return activityOrderNumber;
    }

    public void setActivityOrderNumber(String activityOrderNumber) {
        this.activityOrderNumber = activityOrderNumber;
    }

    public String getActivityIp() {
        return activityIp;
    }

    public void setActivityIp(String activityIp) {
        this.activityIp = activityIp;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getActivityMemberId() {
        return activityMemberId;
    }

    public void setActivityMemberId(Long activityMemberId) {
        this.activityMemberId = activityMemberId;
    }

    public Long getActivityCompanyId() {
        return activityCompanyId;
    }

    public void setActivityCompanyId(Long activityCompanyId) {
        this.activityCompanyId = activityCompanyId;
    }

    public Integer getActivityHasReceive() {
        return activityHasReceive;
    }

    public void setActivityHasReceive(Integer activityHasReceive) {
        this.activityHasReceive = activityHasReceive;
    }

    public Date getActivityCreateTime() {
        return activityCreateTime;
    }

    public void setActivityCreateTime(Date activityCreateTime) {
        this.activityCreateTime = activityCreateTime;
    }

    public Integer getActivityReceive() {
        return activityReceive;
    }

    public void setActivityReceive(Integer activityReceive) {
        this.activityReceive = activityReceive;
    }

    public String getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(String activityNumber) {
        this.activityNumber = activityNumber;
    }
}
