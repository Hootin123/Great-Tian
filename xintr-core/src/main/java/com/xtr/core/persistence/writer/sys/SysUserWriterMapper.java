package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.SysUserBean;

public interface SysUserWriterMapper {
    /**
     *  根据主键删除数据库的记录,sys_user
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,sys_user
     *
     * @param record
     */
    int insert(SysUserBean record);

    /**
     *  动态字段,写入数据库记录,sys_user
     *
     * @param record
     */
    int insertSelective(SysUserBean record);

    /**
     *  根据指定主键获取一条数据库记录,sys_user
     *
     * @param id
     */
    SysUserBean selectByPrimaryKey(Long id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,sys_user
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysUserBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,sys_user
     *
     * @param record
     */
    int updateByPrimaryKey(SysUserBean record);
}