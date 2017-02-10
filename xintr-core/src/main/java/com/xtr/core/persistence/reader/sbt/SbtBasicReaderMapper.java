package com.xtr.core.persistence.reader.sbt;

import com.xtr.api.domain.sbt.SbtBasicBean;
import org.apache.ibatis.annotations.Param;

public interface SbtBasicReaderMapper {

    /**
     * 根据城市和月份查询基础数据列表
     *
     * @param city
     * @param month
     * @return
     */
    SbtBasicBean selectByCityAndMonth(@Param("city") String city, @Param("month") Integer month);

    /**
     * 查询最近的一个月基础数据
     * @param city
     * @param month
     * @return
     */
    SbtBasicBean selectPrevMonth(@Param("city") String city, @Param("month") Integer month);

    /**
     * 查询最后一个月的社保公积金基础数据
     * @param cityCode
     * @return
     */
    SbtBasicBean selectMaxMonthBasic(@Param("city") String cityCode);
}