package com.xtr.core.persistence.reader.sys;

import com.xtr.api.domain.sys.SysMenuBtnBean;

import java.util.List;

public interface SysMenuBtnReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,sys_menu_btn
     *
     * @param id
     */
    SysMenuBtnBean selectByPrimaryKey(Long id);

    /**
     * 根据用户id查询按钮
     *
     * @param userId
     */
    List<SysMenuBtnBean> getMenuBtnByUser(Long userId);


    /**
     * 根据菜单id获取按钮
     *
     * @param menuId
     * @return
     */
    List<SysMenuBtnBean> getMenuBtnByMenuId(Long menuId);

    /**
     * 查询所有按钮
     *
     * @return
     */
    List<SysMenuBtnBean> queryByAll();
}