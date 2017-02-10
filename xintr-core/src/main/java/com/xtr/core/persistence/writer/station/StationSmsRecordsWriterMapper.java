package com.xtr.core.persistence.writer.station;

import com.xtr.api.domain.station.StationSmsRecordsBean;

public interface StationSmsRecordsWriterMapper {
    /**
     *  根据主键删除数据库的记录,station_sms_records
     *
     * @param recordId
     */
    int deleteByPrimaryKey(Long recordId);

    /**
     *  新写入数据库记录,station_sms_records
     *
     * @param record
     */
    int insert(StationSmsRecordsBean record);

    /**
     *  动态字段,写入数据库记录,station_sms_records
     *
     * @param record
     */
    int insertSelective(StationSmsRecordsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,station_sms_records
     *
     * @param record
     */
    int updateByPrimaryKeySelective(StationSmsRecordsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,station_sms_records
     *
     * @param record
     */
    int updateByPrimaryKey(StationSmsRecordsBean record);
}