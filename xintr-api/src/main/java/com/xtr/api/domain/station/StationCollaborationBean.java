package com.xtr.api.domain.station;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class StationCollaborationBean extends BaseObject implements Serializable {
    /**
     *  合作意向表ID,所属表字段为station_collaboration.item_id
     */
    private Long itemId;

    /**
     *  合作意向手机号码,所属表字段为station_collaboration.item_mobile
     */
    private String itemMobile;

    /**
     *  意向商家名,所属表字段为station_collaboration.item_name
     */
    private String itemName;

    /**
     *  备注,所属表字段为station_collaboration.item_bak
     */
    private String itemBak;

    /**
     *  意向类型 1商务合作 2新用户注册 3提交工资单 4签约到期前30天 5融资意向.,所属表字段为station_collaboration.item_type
     */
    private Integer itemType;

    /**
     *  提交时间,所属表字段为station_collaboration.adddate
     */
    private Date adddate;

    /**
     *  状态：1 待联系（默认状态） 2已联系(暂存) 3已签约 4关闭,所属表字段为station_collaboration.sign
     */
    private Integer sign;

    /**
     *  联系的管理员,所属表字段为station_collaboration.admin_user
     */
    private Integer adminUser;

    /**
     *  联系的管理员名称,所属表字段为station_collaboration.admin_username
     */
    private String adminUsername;

    /**
     *  联系结果:电话沟通的内容标题,所属表字段为station_collaboration.item_re_title
     */
    private String itemReTitle;

    /**
     *  小记沟通简单内容,所属表字段为station_collaboration.item_re_cont
     */
    private String itemReCont;

    /**
     *  发起次数 1第一次 2........,所属表字段为station_collaboration.item_times
     */
    private Integer itemTimes;

    /**
     *  处理时间,所属表字段为station_collaboration.recall_time
     */
    private Date recallTime;

    /**
     *  业务经理,所属表字段为station_collaboration.business_ma_name
     */
    private String businessMaName;
    //企业ID
    private Long collaborationCompanyId;
    //关闭后发起的次数
    private Integer collaborationCloseItems;
    //关联的企业名称
    private String companyName ;
    //关联的企业手机号
    private String  memberPhone;
    //关联的注册员工手机号
    private String loginPhone;
    //邀请码
    private String companyContactPlace;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //签约类型：1代发协议 2垫发协议 3社保代缴协议
    private Integer collaborationContractType;
    //合同邮寄地址
    private String collaborationContractAddress;
    //合同内容
    private String collaborationContractContent;
    //企业联系人姓名
    private String collaborationRelationerName;
    //企业联系人电话
    private String collaborationRelationerPhone;

    public Integer getCollaborationContractType() {
        return collaborationContractType;
    }

    public void setCollaborationContractType(Integer collaborationContractType) {
        this.collaborationContractType = collaborationContractType;
    }

    public String getCollaborationContractAddress() {
        return collaborationContractAddress;
    }

    public void setCollaborationContractAddress(String collaborationContractAddress) {
        this.collaborationContractAddress = collaborationContractAddress;
    }

    public String getCollaborationContractContent() {
        return collaborationContractContent;
    }

    public void setCollaborationContractContent(String collaborationContractContent) {
        this.collaborationContractContent = collaborationContractContent;
    }

    public String getCollaborationRelationerName() {
        return collaborationRelationerName;
    }

    public void setCollaborationRelationerName(String collaborationRelationerName) {
        this.collaborationRelationerName = collaborationRelationerName;
    }

    public String getCollaborationRelationerPhone() {
        return collaborationRelationerPhone;
    }

    public void setCollaborationRelationerPhone(String collaborationRelationerPhone) {
        this.collaborationRelationerPhone = collaborationRelationerPhone;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public String getCompanyContactPlace() {
        return companyContactPlace;
    }

    public void setCompanyContactPlace(String companyContactPlace) {
        this.companyContactPlace = companyContactPlace;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public Integer getCollaborationCloseItems() {
        return collaborationCloseItems;
    }

    public void setCollaborationCloseItems(Integer collaborationCloseItems) {
        this.collaborationCloseItems = collaborationCloseItems;
    }

    public Long getCollaborationCompanyId() {
        return collaborationCompanyId;
    }

    public void setCollaborationCompanyId(Long collaborationCompanyId) {
        this.collaborationCompanyId = collaborationCompanyId;
    }

    /**
     * 获取 合作意向表ID 字段:station_collaboration.item_id
     *
     * @return station_collaboration.item_id, 合作意向表ID
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * 设置 合作意向表ID 字段:station_collaboration.item_id
     *
     * @param itemId station_collaboration.item_id, 合作意向表ID
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取 合作意向手机号码 字段:station_collaboration.item_mobile
     *
     * @return station_collaboration.item_mobile, 合作意向手机号码
     */
    public String getItemMobile() {
        return itemMobile;
    }

    /**
     * 设置 合作意向手机号码 字段:station_collaboration.item_mobile
     *
     * @param itemMobile station_collaboration.item_mobile, 合作意向手机号码
     */
    public void setItemMobile(String itemMobile) {
        this.itemMobile = itemMobile == null ? null : itemMobile.trim();
    }

    /**
     * 获取 意向商家名 字段:station_collaboration.item_name
     *
     * @return station_collaboration.item_name, 意向商家名
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * 设置 意向商家名 字段:station_collaboration.item_name
     *
     * @param itemName station_collaboration.item_name, 意向商家名
     */
    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    /**
     * 获取 备注 字段:station_collaboration.item_bak
     *
     * @return station_collaboration.item_bak, 备注
     */
    public String getItemBak() {
        return itemBak;
    }

    /**
     * 设置 备注 字段:station_collaboration.item_bak
     *
     * @param itemBak station_collaboration.item_bak, 备注
     */
    public void setItemBak(String itemBak) {
        this.itemBak = itemBak == null ? null : itemBak.trim();
    }

    /**
     * 获取 意向类型 1商务合作 2..... 字段:station_collaboration.item_type
     *
     * @return station_collaboration.item_type, 意向类型 1商务合作 2.....
     */
    public Integer getItemType() {
        return itemType;
    }

    /**
     * 设置 意向类型 1商务合作 2..... 字段:station_collaboration.item_type
     *
     * @param itemType station_collaboration.item_type, 意向类型 1商务合作 2.....
     */
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取 提交时间 字段:station_collaboration.adddate
     *
     * @return station_collaboration.adddate, 提交时间
     */
    public Date getAdddate() {
        return adddate;
    }

    /**
     * 设置 提交时间 字段:station_collaboration.adddate
     *
     * @param adddate station_collaboration.adddate, 提交时间
     */
    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

    /**
     * 获取 状态：1 待联系（默认状态） 2已联系(暂存) 3已签约 4关闭 字段:station_collaboration.sign
     *
     * @return station_collaboration.sign, 状态：1 待联系（默认状态） 2已联系(暂存) 3已签约 4关闭
     */
    public Integer getSign() {
        return sign;
    }

    /**
     * 设置 状态：1 待联系（默认状态） 2已联系(暂存) 3已签约 4关闭 字段:station_collaboration.sign
     *
     * @param sign station_collaboration.sign, 状态：1 待联系（默认状态） 2已联系(暂存) 3已签约 4关闭
     */
    public void setSign(Integer sign) {
        this.sign = sign;
    }

    /**
     * 获取 联系的管理员 字段:station_collaboration.admin_user
     *
     * @return station_collaboration.admin_user, 联系的管理员
     */
    public Integer getAdminUser() {
        return adminUser;
    }

    /**
     * 设置 联系的管理员 字段:station_collaboration.admin_user
     *
     * @param adminUser station_collaboration.admin_user, 联系的管理员
     */
    public void setAdminUser(Integer adminUser) {
        this.adminUser = adminUser;
    }

    /**
     * 获取 联系的管理员名称 字段:station_collaboration.admin_username
     *
     * @return station_collaboration.admin_username, 联系的管理员名称
     */
    public String getAdminUsername() {
        return adminUsername;
    }

    /**
     * 设置 联系的管理员名称 字段:station_collaboration.admin_username
     *
     * @param adminUsername station_collaboration.admin_username, 联系的管理员名称
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername == null ? null : adminUsername.trim();
    }

    /**
     * 获取 联系结果:电话沟通的内容标题 字段:station_collaboration.item_re_title
     *
     * @return station_collaboration.item_re_title, 联系结果:电话沟通的内容标题
     */
    public String getItemReTitle() {
        return itemReTitle;
    }

    /**
     * 设置 联系结果:电话沟通的内容标题 字段:station_collaboration.item_re_title
     *
     * @param itemReTitle station_collaboration.item_re_title, 联系结果:电话沟通的内容标题
     */
    public void setItemReTitle(String itemReTitle) {
        this.itemReTitle = itemReTitle == null ? null : itemReTitle.trim();
    }

    /**
     * 获取 小记沟通简单内容 字段:station_collaboration.item_re_cont
     *
     * @return station_collaboration.item_re_cont, 小记沟通简单内容
     */
    public String getItemReCont() {
        return itemReCont;
    }

    /**
     * 设置 小记沟通简单内容 字段:station_collaboration.item_re_cont
     *
     * @param itemReCont station_collaboration.item_re_cont, 小记沟通简单内容
     */
    public void setItemReCont(String itemReCont) {
        this.itemReCont = itemReCont == null ? null : itemReCont.trim();
    }

    /**
     * 获取 发起次数 1第一次 2........ 字段:station_collaboration.item_times
     *
     * @return station_collaboration.item_times, 发起次数 1第一次 2........
     */
    public Integer getItemTimes() {
        return itemTimes;
    }

    /**
     * 设置 发起次数 1第一次 2........ 字段:station_collaboration.item_times
     *
     * @param itemTimes station_collaboration.item_times, 发起次数 1第一次 2........
     */
    public void setItemTimes(Integer itemTimes) {
        this.itemTimes = itemTimes;
    }

    /**
     * 获取 处理时间 字段:station_collaboration.recall_time
     *
     * @return station_collaboration.recall_time, 处理时间
     */
    public Date getRecallTime() {
        return recallTime;
    }

    /**
     * 设置 处理时间 字段:station_collaboration.recall_time
     *
     * @param recallTime station_collaboration.recall_time, 处理时间
     */
    public void setRecallTime(Date recallTime) {
        this.recallTime = recallTime;
    }

    /**
     * 获取 业务经理 字段:station_collaboration.business_ma_name
     *
     * @return station_collaboration.business_ma_name, 业务经理
     */
    public String getBusinessMaName() {
        return businessMaName;
    }

    /**
     * 设置 业务经理 字段:station_collaboration.business_ma_name
     *
     * @param businessMaName station_collaboration.business_ma_name, 业务经理
     */
    public void setBusinessMaName(String businessMaName) {
        this.businessMaName = businessMaName == null ? null : businessMaName.trim();
    }
}