package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.SysLogBean;

public interface SysLogWriterMapper {
    /**
     *  根据主键删除数据库的记录,sys_log
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,sys_log
     *
     * @param record
     */
    int insert(SysLogBean record);

    /**
     *  动态字段,写入数据库记录,sys_log
     *
     * @param record
     */
    int insertSelective(SysLogBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,sys_log
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysLogBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,sys_log
     *
     * @param record
     */
    int updateByPrimaryKey(SysLogBean record);
}