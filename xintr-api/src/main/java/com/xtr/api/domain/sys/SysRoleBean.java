package com.xtr.api.domain.sys;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class SysRoleBean extends BaseObject implements Serializable{
    /**
     *  主键,所属表字段为sys_role.id
     */
    private Long id;

    /**
     *  角色名称,所属表字段为sys_role.role_name
     */
    private String roleName;

    /**
     *  创建时间,所属表字段为sys_role.create_time
     */
    private Date createTime;

    /**
     *  创建人,所属表字段为sys_role.create_user
     */
    private Long createUser;

    /**
     *  修改时间,所属表字段为sys_role.update_time
     */
    private Date updateTime;

    /**
     *  修改人,所属表字段为sys_role.update_user
     */
    private Long updateUser;

    /**
     *  状态0=可用 1=禁用,所属表字段为sys_role.state
     */
    private Integer state;

    /**
     *  角色描述,所属表字段为sys_role.descr
     */
    private String descr;

    /**
     * 获取 主键 字段:sys_role.id
     *
     * @return sys_role.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:sys_role.id
     *
     * @param id sys_role.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 角色名称 字段:sys_role.role_name
     *
     * @return sys_role.role_name, 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置 角色名称 字段:sys_role.role_name
     *
     * @param roleName sys_role.role_name, 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取 创建时间 字段:sys_role.create_time
     *
     * @return sys_role.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:sys_role.create_time
     *
     * @param createTime sys_role.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 创建人 字段:sys_role.create_user
     *
     * @return sys_role.create_user, 创建人
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 设置 创建人 字段:sys_role.create_user
     *
     * @param createUser sys_role.create_user, 创建人
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取 修改时间 字段:sys_role.update_time
     *
     * @return sys_role.update_time, 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 字段:sys_role.update_time
     *
     * @param updateTime sys_role.update_time, 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 修改人 字段:sys_role.update_user
     *
     * @return sys_role.update_user, 修改人
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置 修改人 字段:sys_role.update_user
     *
     * @param updateUser sys_role.update_user, 修改人
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 获取 状态0=可用 1=禁用 字段:sys_role.state
     *
     * @return sys_role.state, 状态0=可用 1=禁用
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置 状态0=可用 1=禁用 字段:sys_role.state
     *
     * @param state sys_role.state, 状态0=可用 1=禁用
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取 角色描述 字段:sys_role.descr
     *
     * @return sys_role.descr, 角色描述
     */
    public String getDescr() {
        return descr;
    }

    /**
     * 设置 角色描述 字段:sys_role.descr
     *
     * @param descr sys_role.descr, 角色描述
     */
    public void setDescr(String descr) {
        this.descr = descr == null ? null : descr.trim();
    }
}