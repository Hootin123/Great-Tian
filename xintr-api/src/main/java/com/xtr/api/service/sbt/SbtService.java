package com.xtr.api.service.sbt;

import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;

import java.util.List;

/**
 * <p>社保通接口</p>
 *
 * @author 任齐
 * @createTime: 2016/9/9 10:21.
 */
public interface SbtService {

    //获取缴纳城市缓存Key
    String redis_cities_cache_key = "redis_sbt_cities_cache_key";

    //获取城市基本信息缓存Key
    String redis_basic_cache_key = "redis_sbt_basic_cache_key";

    /**
     * 获取可缴纳城市
     * @return
     */
    List<City> getCities();

    /**
     * 根据城市编码获取当前城市信息
     * @param cityCode
     * @return
     */
    City getCityByCode(String cityCode);

    /**
     * 按城市获取社保公积金基础数据
     * @param cityCode
     * @return
     */
    Basic getBasic(String cityCode);

    /**
     * 获取补缴基础数据
     *
     * @param city      城市: shanghai
     * @param startMon  开始补缴月份：201606
     * @param endMon    截止补缴月份：201608
     * @return
     */
    List<Basic> getSupplementaryPay(String city, String startMon, String endMon);

    /**
     * 获取补缴基础数据
     *
     * @param city      城市: shanghai
     * @param mon  补缴月份：201606
     * @return
     */
    Basic getSupplementaryPay(String city, String mon);

    /**
     * 将数据库数据同步到缓存
     */
    void loadDataFromDB();

    /**
     * 清楚缓存中的数据
     */
    void cleanChace();
}
