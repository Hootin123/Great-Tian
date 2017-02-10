package com.xtr.core.persistence.reader.station;

import com.xtr.api.domain.station.StationSmsRecordsBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface StationSmsRecordsReaderMapper {


    /**
     *  根据指定主键获取一条数据库记录,station_sms_records
     *
     * @param recordId
     */
    StationSmsRecordsBean selectByPrimaryKey(Long recordId);

    /*
    * <p>方法说明</p>
    * @auther 何成彪
    * @createTime2016/7/4 16:51
    */
    long getCountByCondition(@Param("recordPhone") String recordPhone, @Param("payday") String payday);


}