package com.xtr.api.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBean;

import java.util.List;

/**
 * <p>系统菜单服务接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 10:05
 */

public interface SysMenuService {

    /**
     * 根据主键删除数据库的记录,sys_menu
     *
     * @param id
     */
    ResultResponse deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,sys_menu
     *
     * @param record
     */
    ResultResponse saveSysMenu(SysMenuBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_menu
     *
     * @param record
     */
    ResultResponse updateByPrimaryKeySelective(SysMenuBean record);

    /**
     * 根据用户id查询父菜单菜单
     *
     * @param userId
     * @return
     */
    ResultResponse getRootMenuByUser(Long userId);


    /**
     * 根据用户id查询子菜单菜单
     *
     * @param userId
     * @return
     */
    ResultResponse getChildMenuByUser(Long parentId,Long userId);

    /**
     * 获取顶级菜单
     *
     * @return
     */
    List<SysMenuBean> getRootMenu();

    /**
     * 获取子菜单
     *
     * @return
     */
    List<SysMenuBean> getChildMenu();


    /**
     * 根据id查询所有的子按钮
     *
     * @param parentId
     * @return
     */
    List<SysMenuBean> selectChildMenuByPk(Long parentId);


    /**
     * 根据指定主键获取一条数据库记录,sys_menu
     *
     * @param id
     */
    SysMenuBean selectByPrimaryKey(Long id);

    /**
     * 根据父id删除所有子菜单
     *
     * @param parentId
     * @return
     */
    ResultResponse deleteChildMenuByParentId(Long parentId);

    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    ResultResponse selectMenuByUserId(Long userId);

    /**
     * 根据角色Id获取所有菜单
     *
     * @param roleId
     * @return
     */
    List<SysMenuBean> selectMenuByRoleId(Long roleId);

}
