package com.xtr.core.persistence.writer.sbt;

import com.xtr.comm.sbt.api.City;

public interface SbtCityWriterMapper {

    /**
     * 插入一条记录
     * @param record
     * @return
     */
    int insert(City record);

    void deleteAll();
}