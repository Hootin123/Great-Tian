package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.AllowanceSettingBean;

public interface AllowanceSettingWriterMapper {
    /**
     *  根据主键删除数据库的记录,allowance_setting
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,allowance_setting
     *
     * @param record
     */
    int insert(AllowanceSettingBean record);

    /**
     *  动态字段,写入数据库记录,allowance_setting
     *
     * @param record
     */
    int insertSelective(AllowanceSettingBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,allowance_setting
     *
     * @param record
     */
    int updateByPrimaryKeySelective(AllowanceSettingBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,allowance_setting
     *
     * @param record
     */
    int updateByPrimaryKey(AllowanceSettingBean record);
}