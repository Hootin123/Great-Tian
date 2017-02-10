package com.xtr.api.service.station;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.station.StationCollaborationBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/13 13:22
 */
public interface StationCollaborationService {

    /**
     * 根据过滤条件获取合作意向信息
     * @param stationCollaborationBean
     * @return
     */
    ResultResponse selectStationCollaborationInfoPageList(StationCollaborationBean stationCollaborationBean);

    /**
     * 根据ID查询合作意向信息
     * @param itemId
     * @return
     */
    StationCollaborationBean selectStationCollaborationInfoById(Long itemId);

    /**
     *  根据ID更新合作意向数据
     * @param record
     */
    int updateByPrimaryKeySelective(StationCollaborationBean record);

    /**
     * 根据企业ID和意向类型查询合作意向信息
     * @param stationCollaborationBean
     * @return
     */
    List<StationCollaborationBean> selectInfoByCompanyIdAndType(StationCollaborationBean stationCollaborationBean);

    /**
     * 新增合作意向
     * @param record
     * @return
     */
    int insertSelective(StationCollaborationBean record);

    /**
     * 埋点更新发起次数或新增合作意向
     * @param companysBean
     * @return
     */
    ResultResponse updateOpeationType(CompanysBean companysBean, int type)throws Exception;

    /**
     * 企业签约,企业意向自动变为签约状态
     * @param protocolType
     * @param companyId
     * @return
     * @throws Exception
     */
    ResultResponse updateStateAuto(Integer protocolType,Long companyId)throws Exception;

    /**
     * 获取批量导出信息
     * @param stationCollaborationBean
     * @return
     */
    List<StationCollaborationBean> selectInfoByBatch(StationCollaborationBean stationCollaborationBean);

    /**
     * 批量导出意向
     * @param webRoot
     * @param payTemplatePath
     * @param downloadPath
     * @return
     */
    File generatorSalaryExcel(String webRoot, String payTemplatePath, String downloadPath, Date startTime, Date endTime)throws Exception;

    /**
     * 获取已提交申请的明确签约意向
     * @param collaborationCompanyId
     * @return
     */
    List<StationCollaborationBean> selectInfoForAlreadyApply(Long collaborationCompanyId);

    /**
     * 删除申请签约意向
     * @param companyId
     * @param contractType
     * @return
     */
    @Transactional
    int deleteSureProtocol(Map map) throws Exception;
}
