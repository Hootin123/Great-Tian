package com.xtr.api.domain.company;

import com.xtr.api.domain.sys.SysMenuBtnBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CompanyMenuBean implements Serializable {

    /**
     * 主键,所属表字段为company_menu.id
     */
    private Long id;

    /**
     * 菜单名称,所属表字段为company_menu.menu_name
     */
    private String menuName;

    /**
     * 系统url,所属表字段为company_menu.url
     */
    private String url;

    /**
     * 父id 关联company_menu.id,所属表字段为company_menu.parent_id
     */
    private Long parentId;

    /**
     * 创建时间,所属表字段为company_menu.create_time
     */
    private Date createTime;

    /**
     * 修改时间,所属表字段为company_menu.update_time
     */
    private Date updateTime;

    /**
     * 排序,所属表字段为company_menu.sort
     */
    private Integer sort;

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
     * 获取 主键 字段:company_menu.id
     *
     * @return company_menu.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:company_menu.id
     *
     * @param id company_menu.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 菜单名称 字段:company_menu.menu_name
     *
     * @return company_menu.menu_name, 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置 菜单名称 字段:company_menu.menu_name
     *
     * @param menuName company_menu.menu_name, 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    /**
     * 获取 系统url 字段:company_menu.url
     *
     * @return company_menu.url, 系统url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置 系统url 字段:company_menu.url
     *
     * @param url company_menu.url, 系统url
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取 父id 关联company_menu.id 字段:company_menu.parent_id
     *
     * @return company_menu.parent_id, 父id 关联company_menu.id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置 父id 关联company_menu.id 字段:company_menu.parent_id
     *
     * @param parentId company_menu.parent_id, 父id 关联company_menu.id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取 创建时间 字段:company_menu.create_time
     *
     * @return company_menu.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:company_menu.create_time
     *
     * @param createTime company_menu.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改时间 字段:company_menu.update_time
     *
     * @return company_menu.update_time, 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 字段:company_menu.update_time
     *
     * @param updateTime company_menu.update_time, 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 排序 字段:company_menu.sort
     *
     * @return company_menu.sort, 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 排序 字段:company_menu.sort
     *
     * @param sort company_menu.sort, 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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