package com.xtr.core.persistence.writer.station;

import com.xtr.api.domain.station.StationCollaborationBean;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface StationCollaborationWriterMapper {
    /**
     *  根据主键删除数据库的记录,station_collaboration
     *
     * @param itemId
     */
    int deleteByPrimaryKey(Long itemId);

    /**
     *  新写入数据库记录,station_collaboration
     *
     * @param record
     */
    int insert(StationCollaborationBean record);

    /**
     *  动态字段,写入数据库记录,station_collaboration
     *
     * @param record
     */
    int insertSelective(StationCollaborationBean record);


    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,station_collaboration
     *
     * @param record
     */
    int updateByPrimaryKeySelective(StationCollaborationBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,station_collaboration
     *
     * @param record
     */
    int updateByPrimaryKey(StationCollaborationBean record);

    /**
     * 撤销申请签约意向
     * @param companyId
     * @param contractType
     * @return
     */
    int deleteSureProtocol(Map map);
}