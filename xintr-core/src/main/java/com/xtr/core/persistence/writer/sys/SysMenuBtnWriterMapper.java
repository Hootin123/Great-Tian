package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.SysMenuBtnBean;

public interface SysMenuBtnWriterMapper {
    /**
     * 根据主键删除数据库的记录,sys_menu_btn
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,sys_menu_btn
     *
     * @param record
     */
    int insert(SysMenuBtnBean record);

    /**
     * 动态字段,写入数据库记录,sys_menu_btn
     *
     * @param record
     */
    int insertSelective(SysMenuBtnBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_menu_btn
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysMenuBtnBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,sys_menu_btn
     *
     * @param record
     */
    int updateByPrimaryKey(SysMenuBtnBean record);

    /**
     * 根据菜单主键删除按钮
     *
     * @param menuId
     * @return
     */
    int deleteByMenuId(Long menuId);
}