package com.xtr.api.domain.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SysMenuBean implements Serializable {

    /**
     * 主键,所属表字段为sys_menu.id
     */
    private Long id;

    /**
     * 菜单名称,所属表字段为sys_menu.menu_name
     */
    private String menuName;

    /**
     * 系统url,所属表字段为sys_menu.url
     */
    private String url;

    /**
     * 父id 关联sys_menu.id,所属表字段为sys_menu.parent_id
     */
    private Long parentId;

    /**
     * 是否删除,0=未删除，1=已删除,所属表字段为sys_menu.is_delete
     */
    private Integer isDelete;

    /**
     * 创建时间,所属表字段为sys_menu.create_time
     */
    private Date createTime;

    /**
     * 修改时间,所属表字段为sys_menu.update_time
     */
    private Date updateTime;

    /**
     * 排序,所属表字段为sys_menu.sort
     */
    private Integer sort;

    /**
     * 注册Action 按钮|分隔,所属表字段为sys_menu.actions
     */
    private String actions;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 子菜单数量
     */
    private String childCount;

    /**
     * 菜单按钮
     */
    private List<SysMenuBtnBean> menuBtnBeanList;

    /**
     * 获取 主键 字段:sys_menu.id
     *
     * @return sys_menu.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:sys_menu.id
     *
     * @param id sys_menu.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 菜单名称 字段:sys_menu.menu_name
     *
     * @return sys_menu.menu_name, 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置 菜单名称 字段:sys_menu.menu_name
     *
     * @param menuName sys_menu.menu_name, 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    /**
     * 获取 系统url 字段:sys_menu.url
     *
     * @return sys_menu.url, 系统url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置 系统url 字段:sys_menu.url
     *
     * @param url sys_menu.url, 系统url
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取 父id 关联sys_menu.id 字段:sys_menu.parent_id
     *
     * @return sys_menu.parent_id, 父id 关联sys_menu.id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置 父id 关联sys_menu.id 字段:sys_menu.parent_id
     *
     * @param parentId sys_menu.parent_id, 父id 关联sys_menu.id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取 是否删除,0=未删除，1=已删除 字段:sys_menu.is_delete
     *
     * @return sys_menu.is_delete, 是否删除,0=未删除，1=已删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置 是否删除,0=未删除，1=已删除 字段:sys_menu.is_delete
     *
     * @param isDelete sys_menu.is_delete, 是否删除,0=未删除，1=已删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取 创建时间 字段:sys_menu.create_time
     *
     * @return sys_menu.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:sys_menu.create_time
     *
     * @param createTime sys_menu.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改时间 字段:sys_menu.update_time
     *
     * @return sys_menu.update_time, 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 字段:sys_menu.update_time
     *
     * @param updateTime sys_menu.update_time, 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 排序 字段:sys_menu.sort
     *
     * @return sys_menu.sort, 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 排序 字段:sys_menu.sort
     *
     * @param sort sys_menu.sort, 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取 注册Action 按钮|分隔 字段:sys_menu.actions
     *
     * @return sys_menu.actions, 注册Action 按钮|分隔
     */
    public String getActions() {
        return actions;
    }

    /**
     * 设置 注册Action 按钮|分隔 字段:sys_menu.actions
     *
     * @param actions sys_menu.actions, 注册Action 按钮|分隔
     */
    public void setActions(String actions) {
        this.actions = actions == null ? null : actions.trim();
    }


    public List<SysMenuBtnBean> getMenuBtnBeanList() {
        return menuBtnBeanList;
    }

    public void setMenuBtnBeanList(List<SysMenuBtnBean> menuBtnBeanList) {
        this.menuBtnBeanList = menuBtnBeanList;
    }

    public String getChildCount() {
        return childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
}