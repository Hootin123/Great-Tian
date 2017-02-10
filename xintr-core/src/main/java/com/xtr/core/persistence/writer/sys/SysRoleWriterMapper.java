package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.SysRoleBean;

public interface SysRoleWriterMapper {
    /**
     *  根据主键删除数据库的记录,sys_role
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,sys_role
     *
     * @param record
     */
    int insert(SysRoleBean record);

    /**
     *  动态字段,写入数据库记录,sys_role
     *
     * @param record
     */
    int insertSelective(SysRoleBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,sys_role
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysRoleBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,sys_role
     *
     * @param record
     */
    int updateByPrimaryKey(SysRoleBean record);

}