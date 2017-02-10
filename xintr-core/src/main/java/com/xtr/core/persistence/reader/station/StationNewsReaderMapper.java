package com.xtr.core.persistence.reader.station;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.station.StationNewsBean;

import java.util.List;

public interface StationNewsReaderMapper {

    /**
     * 根据主键查询新闻
     *
     * @param newsId
     * @return
     */
    StationNewsBean selectByPrimaryKey(Long newsId);

    List<StationNewsBean> selectList(StationNewsBean stationNewsBean);

    PageList<StationNewsBean> selectPageList(StationNewsBean stationNewsBean, PageBounds pageBounds);

}