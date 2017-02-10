package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.BonusSettingsBean;

public interface BonusSettingsWriterMapper {
    /**
     *  根据主键删除数据库的记录,bonus_settings
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,bonus_settings
     *
     * @param record
     */
    int insert(BonusSettingsBean record);

    /**
     *  动态字段,写入数据库记录,bonus_settings
     *
     * @param record
     */
    int insertSelective(BonusSettingsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,bonus_settings
     *
     * @param record
     */
    int updateByPrimaryKeySelective(BonusSettingsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,bonus_settings
     *
     * @param record
     */
    int updateByPrimaryKey(BonusSettingsBean record);
}