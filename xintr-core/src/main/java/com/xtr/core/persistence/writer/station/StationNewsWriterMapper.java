package com.xtr.core.persistence.writer.station;

import com.xtr.api.domain.station.StationNewsBean;

public interface StationNewsWriterMapper {

    /**
     * 根据主键删除新闻
     *
     * @param newsId
     * @return
     */
    int deleteByPrimaryKey(Long newsId);

    /**
     * 插入一条新闻
     *
     * @param record
     * @return
     */
    int insert(StationNewsBean record);

    /**
     * 插入一条新闻
     *
     * @param record
     * @return
     */
    int insertSelective(StationNewsBean record);

    /**
     * 更新新闻
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(StationNewsBean record);

    /**
     * 根据主键更新新闻
     * 
     * @param record
     * @return
     */
    int updateByPrimaryKey(StationNewsBean record);
}