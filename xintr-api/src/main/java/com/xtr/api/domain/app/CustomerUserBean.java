package com.xtr.api.domain.app;

import java.io.Serializable;
import java.util.Date;

public class CustomerUserBean implements Serializable{
    /**
     * 主键,所属表字段为customer_user.id
     */
    private Long id;

    /**
     * 手机也是登录账号,所属表字段为customer_user.mobile_phone
     */
    private String mobilePhone;

    /**
     * 邮箱也是登录帐号,所属表字段为customer_user.email
     */
    private String email;

    /**
     * 身份证号码,所属表字段为customer_user.id_card
     */
    private String idCard;

    /**
     * 登录密码,所属表字段为customer_user.pwd
     */
    private String pwd;

    /**
     * 状态 0=可用,1=禁用,所属表字段为customer_user.state
     */
    private String state;

    /**
     * 登录总次数,所属表字段为customer_user.login_count
     */
    private Integer loginCount;

    /**
     * 最后登录时间,所属表字段为customer_user.login_time
     */
    private Date loginTime;

    /**
     * 删除状态 0=未删除,1=已删除,所属表字段为customer_user.is_delete
     */
    private Integer isDelete;

    /**
     * 创建时间,所属表字段为customer_user.create_time
     */
    private Date createTime;

    /**
     * 修改时间,所属表字段为customer_user.update_time
     */
    private Date updateTime;

    /**
     * 密码加密,所属表字段为customer_user.salt
     */
    private String salt;

    /**
     * 是否关联企业员工 0-未关联，1-已关联,所属表字段为customer_user.is_matching
     */
    private Integer isMatching;

    /**
     * 企业员工Id
     */
    private Long customerId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String fullName;

    /**
     * 头像url
     */
    private String headUrl;


    /**
     * 获取 主键 字段:customer_user.id
     *
     * @return customer_user.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:customer_user.id
     *
     * @param id customer_user.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 手机也是登录账号 字段:customer_user.mobile_phone
     *
     * @return customer_user.mobile_phone, 手机也是登录账号
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 设置 手机也是登录账号 字段:customer_user.mobile_phone
     *
     * @param mobilePhone customer_user.mobile_phone, 手机也是登录账号
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    /**
     * 获取 邮箱也是登录帐号 字段:customer_user.email
     *
     * @return customer_user.email, 邮箱也是登录帐号
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置 邮箱也是登录帐号 字段:customer_user.email
     *
     * @param email customer_user.email, 邮箱也是登录帐号
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取 身份证号码 字段:customer_user.id_card
     *
     * @return customer_user.id_card, 身份证号码
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置 身份证号码 字段:customer_user.id_card
     *
     * @param idCard customer_user.id_card, 身份证号码
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    /**
     * 获取 登录密码 字段:customer_user.pwd
     *
     * @return customer_user.pwd, 登录密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置 登录密码 字段:customer_user.pwd
     *
     * @param pwd customer_user.pwd, 登录密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * 获取 状态 0=可用,1=禁用 字段:customer_user.state
     *
     * @return customer_user.state, 状态 0=可用,1=禁用
     */
    public String getState() {
        return state;
    }

    /**
     * 设置 状态 0=可用,1=禁用 字段:customer_user.state
     *
     * @param state customer_user.state, 状态 0=可用,1=禁用
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * 获取 登录总次数 字段:customer_user.login_count
     *
     * @return customer_user.login_count, 登录总次数
     */
    public Integer getLoginCount() {
        return loginCount;
    }

    /**
     * 设置 登录总次数 字段:customer_user.login_count
     *
     * @param loginCount customer_user.login_count, 登录总次数
     */
    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    /**
     * 获取 最后登录时间 字段:customer_user.login_time
     *
     * @return customer_user.login_time, 最后登录时间
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置 最后登录时间 字段:customer_user.login_time
     *
     * @param loginTime customer_user.login_time, 最后登录时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * 获取 删除状态 0=未删除,1=已删除 字段:customer_user.is_delete
     *
     * @return customer_user.is_delete, 删除状态 0=未删除,1=已删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置 删除状态 0=未删除,1=已删除 字段:customer_user.is_delete
     *
     * @param isDelete customer_user.is_delete, 删除状态 0=未删除,1=已删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取 创建时间 字段:customer_user.create_time
     *
     * @return customer_user.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:customer_user.create_time
     *
     * @param createTime customer_user.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改时间 字段:customer_user.update_time
     *
     * @return customer_user.update_time, 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 字段:customer_user.update_time
     *
     * @param updateTime customer_user.update_time, 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 密码加密 字段:customer_user.salt
     *
     * @return customer_user.salt, 密码加密
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置 密码加密 字段:customer_user.salt
     *
     * @param salt customer_user.salt, 密码加密
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * 获取 是否关联企业员工 0-未关联，1-已关联 字段:customer_user.is_matching
     *
     * @return customer_user.is_matching, 是否关联企业员工 0-未关联，1-已关联
     */
    public Integer getIsMatching() {
        return isMatching;
    }

    /**
     * 设置 是否关联企业员工 0-未关联，1-已关联 字段:customer_user.is_matching
     *
     * @param isMatching customer_user.is_matching, 是否关联企业员工 0-未关联，1-已关联
     */
    public void setIsMatching(Integer isMatching) {
        this.isMatching = isMatching;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}