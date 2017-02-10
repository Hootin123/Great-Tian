package com.xtr.core.persistence.reader.station;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.station.StationCollaborationBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StationCollaborationReaderMapper {


    /**
     *  根据过滤条件查询合作意向信息(分页)
     * @param stationCollaborationBean
     */
    List<StationCollaborationBean> selectStationCollaborationInfoPageList(StationCollaborationBean stationCollaborationBean,PageBounds pageBounds);

    /**
     * 根据ID查询合作意向信息
     * @param itemId
     * @return
     */
    StationCollaborationBean selectStationCollaborationInfoById(Long itemId);

    /**
     * 根据企业ID和意向类型查询合作意向信息
     * @param stationCollaborationBean
     * @return
     */
    List<StationCollaborationBean> selectInfoByCompanyIdAndType(StationCollaborationBean stationCollaborationBean);
    /**
     *  根据过滤条件查询合作意向信息
     * @param stationCollaborationBean
     */
    List<StationCollaborationBean> selectStationCollaborationInfoPageList(StationCollaborationBean stationCollaborationBean);

    /**
     * 获取批量导出信息
     * @param stationCollaborationBean
     * @return
     */
    List<StationCollaborationBean> selectInfoByBatch(StationCollaborationBean stationCollaborationBean);

    /**
     * 获取已提交申请的明确签约意向
     * @param collaborationCompanyId
     * @return
     */
    List<StationCollaborationBean> selectInfoForAlreadyApply(Long collaborationCompanyId);


}