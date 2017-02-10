package com.xtr.api.service.station;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.station.StationNewsBean;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * <p>新闻</p>
 *
 * @author 任齐
 * @createTime: 2016/7/18 11:31
 */
public interface StationNewsService {

    /**
     * 根据新闻id查询新闻
     *
     * @param newId
     * @return
     */
    StationNewsBean selecyByNewId(Long newId);

    /**
     * 保存新闻
     *
     * @param stationNewsBean
     * @return
     */
    void saveStationNews(StationNewsBean stationNewsBean) throws BusinessException;

    /**
     * 修改新闻
     *
     * @param stationNewsBean
     * @throws BusinessException
     */
    void updateStationNews(StationNewsBean stationNewsBean) throws BusinessException;

    /**
     * 查询新闻列表
     *
     * @param newsType
     * @param newsState
     * @return
     */
    List<StationNewsBean> selectList(Integer newsType, Integer newsState);

    /**
     * 分页查询新闻列表
     *
     * @param newsType
     * @param newsState
     * @param newsTitle
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultResponse selectPageList(Integer newsType, Integer newsState, String newsTitle, int pageIndex, int pageSize);

    /**
     * 修改新闻发布状态
     *
     * @param newsId
     * @param state
     * @throws BusinessException
     */
    void republish(Long newsId, Integer state) throws BusinessException;
}
