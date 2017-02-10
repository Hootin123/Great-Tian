package com.xtr.core.persistence.writer.sbt;

import com.xtr.api.domain.sbt.SbtBasicBean;

public interface SbtBasicWriterMapper {

    /**
     * 插入一条数据
     * @param record
     * @return
     */
    int insert(SbtBasicBean record);

    /**
     * 插入一条数据
     * @param record
     * @return
     */
    int insertSelective(SbtBasicBean record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SbtBasicBean record);

    /**
     * 更新一条记录
     * @param record
     * @return
     */
    int updateDataByPrimaryKey(SbtBasicBean record);
}