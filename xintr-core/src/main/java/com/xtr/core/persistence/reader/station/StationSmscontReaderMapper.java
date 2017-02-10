package com.xtr.core.persistence.reader.station;

import com.xtr.api.domain.station.StationSmscontBean;

public interface StationSmscontReaderMapper {
    /**
     *  根据指定主键获取一条数据库记录,station_smscont
     *
     * @param smsId
     */
    StationSmscontBean selectByPrimaryKey(Integer smsId);

    /**
     * 根据短信类型获取短信模板
     * @param smsType
     * @return
     */
    StationSmscontBean selectBySmsType(Integer smsType);
}