package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.SysMenuBean;

public interface SysMenuWriterMapper {
    /**
     * 根据主键删除数据库的记录,sys_menu
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,sys_menu
     *
     * @param record
     */
    int insert(SysMenuBean record);

    /**
     * 动态字段,写入数据库记录,sys_menu
     *
     * @param record
     */
    int insertSelective(SysMenuBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_menu
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysMenuBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,sys_menu
     *
     * @param record
     */
    int updateByPrimaryKey(SysMenuBean record);

}