package com.xtr.core.persistence.reader.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.sys.SysMenuDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,sys_menu
     *
     * @param id
     */
    SysMenuBean selectByPrimaryKey(Long id);

    /**
     * 根据用户id查询父菜单菜单
     *
     * @param userId
     * @return
     */
    List<SysMenuBean> getRootMenuByUser(@Param("userId") Long userId);

    /**
     * 根据用户id查询子菜单菜单
     *
     * @param userId
     * @return
     */
    List<SysMenuBean> getChildMenuByUser(@Param("parentId") Long parentId,@Param("userId") Long userId);

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
     * 根据父id删除所有子菜单
     *
     * @param parentId
     * @return
     */
    int deleteChildMenuByParentId(Long parentId);

    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    List<SysMenuDto> selectMenuByUserId(Long userId);

}