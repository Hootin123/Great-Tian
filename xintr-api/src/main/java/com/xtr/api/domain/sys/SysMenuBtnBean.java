package com.xtr.api.domain.sys;

import java.io.Serializable;

public class SysMenuBtnBean implements Serializable {
    /**
     * 主键,所属表字段为sys_menu_btn.id
     */
    private Long id;

    /**
     * 菜单id关联 sys_menu.id,所属表字段为sys_menu_btn.menu_id
     */
    private Long menuId;

    /**
     * 按钮名称,所属表字段为sys_menu_btn.btn_name
     */
    private String btnName;

    /**
     * 按钮类型,所属表字段为sys_menu_btn.btn_type
     */
    private String btnType;

    /**
     * url注册，用"," 分隔 。用于权限控制UR,所属表字段为sys_menu_btn.action_urls
     */
    private String actionUrls;

    /**
     * 删除标记，与数据库字段无关 1=删除,其他不删除
     */
    private String deleteFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 获取 主键 字段:sys_menu_btn.id
     *
     * @return sys_menu_btn.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:sys_menu_btn.id
     *
     * @param id sys_menu_btn.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 菜单id关联 sys_menu.id 字段:sys_menu_btn.menu_id
     *
     * @return sys_menu_btn.menu_id, 菜单id关联 sys_menu.id
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置 菜单id关联 sys_menu.id 字段:sys_menu_btn.menu_id
     *
     * @param menuId sys_menu_btn.menu_id, 菜单id关联 sys_menu.id
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取 按钮名称 字段:sys_menu_btn.btn_name
     *
     * @return sys_menu_btn.btn_name, 按钮名称
     */
    public String getBtnName() {
        return btnName;
    }

    /**
     * 设置 按钮名称 字段:sys_menu_btn.btn_name
     *
     * @param btnName sys_menu_btn.btn_name, 按钮名称
     */
    public void setBtnName(String btnName) {
        this.btnName = btnName == null ? null : btnName.trim();
    }

    /**
     * 获取 按钮类型 字段:sys_menu_btn.btn_type
     *
     * @return sys_menu_btn.btn_type, 按钮类型
     */
    public String getBtnType() {
        return btnType;
    }

    /**
     * 设置 按钮类型 字段:sys_menu_btn.btn_type
     *
     * @param btnType sys_menu_btn.btn_type, 按钮类型
     */
    public void setBtnType(String btnType) {
        this.btnType = btnType == null ? null : btnType.trim();
    }

    /**
     * 获取 url注册，用"," 分隔 。用于权限控制UR 字段:sys_menu_btn.action_urls
     *
     * @return sys_menu_btn.action_urls, url注册，用"," 分隔 。用于权限控制UR
     */
    public String getActionUrls() {
        return actionUrls;
    }

    /**
     * 设置 url注册，用"," 分隔 。用于权限控制UR 字段:sys_menu_btn.action_urls
     *
     * @param actionUrls sys_menu_btn.action_urls, url注册，用"," 分隔 。用于权限控制UR
     */
    public void setActionUrls(String actionUrls) {
        this.actionUrls = actionUrls == null ? null : actionUrls.trim();
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}