package com.xtr.core.service.sbt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.xtr.api.domain.sbt.SbtBasicBean;
import com.xtr.api.service.sbt.SbtBasicService;
import com.xtr.api.service.sbt.SbtCityService;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.comm.jd.util.StringUtils;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>社保通服务实现</p>
 *
 * @author 任齐
 * @createTime: 2016/9/9 10:36.
 */
@Service("sbtService")
public class SbtServiceImpl implements SbtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SbtService.class);

    // 社保通sdk
    private SheBaoTong sheBaoTong = new SheBaoTong(true);

    @Resource
    private SbtCityService sbtCityService;

    @Resource
    private SbtBasicService sbtBasicService;

    @Resource
    private RedisTemplate redisTemplate;



    /**
     * 获取可缴纳城市
     * @return
     */
    @Override
    public synchronized List<City> getCities() {
        try {
            ValueOperations<String, List<City>> valueOperations = redisTemplate.opsForValue();
            List<City> cities = valueOperations.get(redis_cities_cache_key);
            if(cities == null) {
                cities = sheBaoTong.getCities();
                valueOperations.set(redis_cities_cache_key, cities);
                if(cities != null){
                    sbtCityService.deleteAll();
                    for (City city : cities) {
                        sbtCityService.saveCity(city);
                    }
                }
            }
            return cities;
        } catch (IOException e) {
            LOGGER.error("获取可缴纳城市发生异常", e);
        }
        return null;
    }

    /**
     * 根据城市编码获取当前城市信息
     * @param cityCode
     * @return
     */
    @Override
    public City getCityByCode(String cityCode) {
        try {
            List<City> cities = getCities();
            for(City city : cities){
                if(city.getCity().equals(cityCode)){
                    return city;
                }
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 按城市获取社保公积金基础数据
     * @param cityCode
     * @return
     */
    @Override
    public Basic getBasic(String cityCode) {
        if(StringUtils.isBlank(cityCode)) return null;
        try {
            String cacheKey = redis_basic_cache_key + cityCode;
            ValueOperations<String, Basic> valueOperations = redisTemplate.opsForValue();
            Basic basic = valueOperations.get(cacheKey);
            if(basic == null) {
                basic = sheBaoTong.getBasic(cityCode);
                valueOperations.set(cacheKey, basic);
                if(basic != null) {
                    //更新数据库现有数据
                    City city = getCityByCode(cityCode);
                    int month = Integer.valueOf(city.getMonth().trim());
                    SbtBasicBean sbtBasicBean = sbtBasicService.getByCityAndMonth(cityCode, month);
                    if (null == sbtBasicBean) {
                        sbtBasicService.saveBasic(cityCode, month, JSON.toJSONString(basic));
                    } else {
                        sbtBasicBean.setData(JSON.toJSONString(basic));
                        sbtBasicService.updateBasic(sbtBasicBean);
                    }
                }

            }
            return basic;
        } catch (IOException e) {
            LOGGER.error("获取社保公积金基础数据发生异常", e);
        }
        return null;
    }

    /**
     * 获取补缴基础数据
     *
     * @param city      城市: shanghai
     * @param startMon  开始补缴月份：201606
     * @param endMon    截止补缴月份：201608
     * @return
     */
    @Override
    public List<Basic> getSupplementaryPay(String city, String startMon, String endMon) {
        List<Basic> basics = new ArrayList<>();

        Basic currentBasic = getBasic(city);

        Calendar startMonth = Calendar.getInstance();
        startMonth.set(Calendar.YEAR, Integer.valueOf(startMon.substring(0, 4)));
        startMonth.set(Calendar.DAY_OF_MONTH, 1);
        startMonth.set(Calendar.MONTH, Integer.valueOf(startMon.substring(4)));

        Calendar endMonth = Calendar.getInstance();
        endMonth.set(Calendar.YEAR, Integer.valueOf(endMon.substring(0, 4)));
        endMonth.set(Calendar.DAY_OF_MONTH, 1);
        endMonth.set(Calendar.MONTH, Integer.valueOf(endMon.substring(4)));

        for(;startMonth.getTimeInMillis() <= endMonth.getTimeInMillis(); endMonth.add(Calendar.MONTH, -1)){
            int year = endMonth.get(Calendar.YEAR);
            int month = endMonth.get(Calendar.MONTH);
            int yearMonth = (year * 100 + month);
            SbtBasicBean sbtBasicBean = sbtBasicService.getByCityAndMonth(city, (year * 100 + month));
            if(null != sbtBasicBean){
                Basic basic = JSON.parseObject(sbtBasicBean.getData(), Basic.class);
                basic.setMonth(yearMonth);
                basics.add(basic);
                currentBasic = JSON.parseObject(sbtBasicBean.getData(), Basic.class);
            }else {
                currentBasic.setMonth(yearMonth);
                String json = JSONObject.toJSONString(currentBasic);
                basics.add(JSON.parseObject(json, Basic.class));
            }

        }
        Collections.reverse(basics);
        return basics;
    }

    @Override
    public Basic getSupplementaryPay(String city, String mon) {
        SbtBasicBean sbtBasicBean = sbtBasicService.getByCityAndMonth(city, Integer.valueOf(mon));
        if(null != sbtBasicBean){
            return JSON.parseObject(sbtBasicBean.getData(), Basic.class);
        }else {
            return getBasic(city);
        }
    }

    public void loadDataFromDB() {
        List<City> cities = sbtCityService.getCities();
        redisTemplate.opsForValue().set(redis_cities_cache_key, cities);

        if(cities != null) {
            for (City city : cities) {
                SbtBasicBean sbtBasicBean = sbtBasicService.getByCityAndMonth(city.getCity(), Integer.valueOf(city.getMonth()));
                if(sbtBasicBean != null) {
                    Basic basic = JSON.parseObject(sbtBasicBean.getData(), Basic.class);
                    redisTemplate.opsForValue().set(redis_basic_cache_key + city.getCity(), basic);
                }else{
                    redisTemplate.opsForValue().set(redis_basic_cache_key + city.getCity(), null);
                }
            }
        }

    }

    @Override
    public void cleanChace() {
        redisTemplate.delete(redis_cities_cache_key);
        redisTemplate.delete(redisTemplate.keys(redis_basic_cache_key+"*"));
    }

}