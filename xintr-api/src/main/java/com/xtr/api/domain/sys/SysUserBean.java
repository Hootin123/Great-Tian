package com.xtr.api.domain.sys;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class SysUserBean extends BaseObject implements Serializable {
    /**
     * 主键,所属表字段为sys_user.id
     */
    private Long id;

    /**
     * 手机号码,所属表字段为sys_user.mobile_phone
     */
    private String mobilePhone;

    /**
     * 邮箱地址,所属表字段为sys_user.email
     */
    private String email;

    /**
     * 用户名,所属表字段为sys_user.user_name
     */
    private String userName;

    /**
     * 昵称,所属表字段为sys_user.nick_name
     */
    private String nickName;

    /**
     * 状态 0=可用,1=禁用,所属表字段为sys_user.state
     */
    private Integer state;

    /**
     * 登录总次数,所属表字段为sys_user.login_count
     */
    private Integer loginCount;

    /**
     * 最后登录时间,所属表字段为sys_user.login_time
     */
    private Date loginTime;

    /**
     * 删除状态 0=未删除,1=已删除,所属表字段为sys_user.is_delete
     */
    private Integer isDelete = 0;

    /**
     * 创建时间,所属表字段为sys_user.create_time
     */
    private Date createTime;

    /**
     * 修改时间,所属表字段为sys_user.update_time
     */
    private Date updateTime;

    /**
     * 创建人,所属表字段为sys_user.create_user
     */
    private Long createUser;

    /**
     * 修改人,所属表字段为sys_user.update_user
     */
    private Long updateUser;

    /**
     * 是否超级管理员 0= 不是，1=是
     */
    private Integer superAdmin;

    /**
     * 登录密码
     */
    private String pwd;

    /**
     * 密码加密
     */
    private String salt;

    /**
     * 获取 主键 字段:sys_user.id
     *
     * @return sys_user.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:sys_user.id
     *
     * @param id sys_user.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 手机号码 字段:sys_user.mobile_phone
     *
     * @return sys_user.mobile_phone, 手机号码
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 设置 手机号码 字段:sys_user.mobile_phone
     *
     * @param mobilePhone sys_user.mobile_phone, 手机号码
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    /**
     * 获取 邮箱地址 字段:sys_user.email
     *
     * @return sys_user.email, 邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置 邮箱地址 字段:sys_user.email
     *
     * @param email sys_user.email, 邮箱地址
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取 用户名 字段:sys_user.user_name
     *
     * @return sys_user.user_name, 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置 用户名 字段:sys_user.user_name
     *
     * @param userName sys_user.user_name, 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取 昵称 字段:sys_user.nick_name
     *
     * @return sys_user.nick_name, 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置 昵称 字段:sys_user.nick_name
     *
     * @param nickName sys_user.nick_name, 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取 状态 0=可用,1=禁用 字段:sys_user.state
     *
     * @return sys_user.state, 状态 0=可用,1=禁用
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置 状态 0=可用,1=禁用 字段:sys_user.state
     *
     * @param state sys_user.state, 状态 0=可用,1=禁用
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取 登录总次数 字段:sys_user.login_count
     *
     * @return sys_user.login_count, 登录总次数
     */
    public Integer getLoginCount() {
        return loginCount;
    }

    /**
     * 设置 登录总次数 字段:sys_user.login_count
     *
     * @param loginCount sys_user.login_count, 登录总次数
     */
    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    /**
     * 获取 最后登录时间 字段:sys_user.login_time
     *
     * @return sys_user.login_time, 最后登录时间
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置 最后登录时间 字段:sys_user.login_time
     *
     * @param loginTime sys_user.login_time, 最后登录时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * 获取 删除状态 0=未删除,1=已删除 字段:sys_user.is_delete
     *
     * @return sys_user.is_delete, 删除状态 0=未删除,1=已删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置 删除状态 0=未删除,1=已删除 字段:sys_user.is_delete
     *
     * @param isDelete sys_user.is_delete, 删除状态 0=未删除,1=已删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取 创建时间 字段:sys_user.create_time
     *
     * @return sys_user.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:sys_user.create_time
     *
     * @param createTime sys_user.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改时间 字段:sys_user.update_time
     *
     * @return sys_user.update_time, 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 字段:sys_user.update_time
     *
     * @param updateTime sys_user.update_time, 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 创建人 字段:sys_user.create_user
     *
     * @return sys_user.create_user, 创建人
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 设置 创建人 字段:sys_user.create_user
     *
     * @param createUser sys_user.create_user, 创建人
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取 修改人 字段:sys_user.update_user
     *
     * @return sys_user.update_user, 修改人
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置 修改人 字段:sys_user.update_user
     *
     * @param updateUser sys_user.update_user, 修改人
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Integer superAdmin) {
        this.superAdmin = superAdmin;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}