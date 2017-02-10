package com.xtr.core.persistence.writer.station;

import com.xtr.api.domain.station.StationSmscontBean;

public interface StationSmscontWriterMapper {
    /**
     *  根据主键删除数据库的记录,station_smscont
     *
     * @param smsId
     */
    int deleteByPrimaryKey(Integer smsId);

    /**
     *  新写入数据库记录,station_smscont
     *
     * @param record
     */
    int insert(StationSmscontBean record);

    /**
     *  动态字段,写入数据库记录,station_smscont
     *
     * @param record
     */
    int insertSelective(StationSmscontBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,station_smscont
     *
     * @param record
     */
    int updateByPrimaryKeySelective(StationSmscontBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,station_smscont
     *
     * @param record
     */
    int updateByPrimaryKey(StationSmscontBean record);
}