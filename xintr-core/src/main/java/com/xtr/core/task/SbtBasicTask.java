package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.sbt.SbtBasicBean;
import com.xtr.api.service.sbt.SbtBasicService;
import com.xtr.api.service.sbt.SbtCityService;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;
import com.xtr.comm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>社保通基础数据存储到数据库</p>
 *
 * @author 任齐
 * @createTime: 2016/9/18 14:30.
 */
@Service("sbtBasicTask")
public class SbtBasicTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SbtBasicTask.class);

    @Resource
    private SbtBasicService sbtBasicService;

    @Resource
    private SbtCityService sbtCityService;

    @Resource
    private RedisTemplate redisTemplate;

    private SheBaoTong sheBaoTong = new SheBaoTong(true);

    @Override
    public void run() throws Exception {
        LOGGER.info("开始存储社保通数据");
        synchronized (this) {
            List<City> cities = sheBaoTong.getCities();
            if (cities != null) {
                LOGGER.info("社保通Cities【" + JSON.toJSONString(cities) + "】");
                redisTemplate.opsForValue().set(SbtService.redis_cities_cache_key, cities);
                sbtCityService.deleteAll();
                for (City city : cities) {
                    //保存城市信息
                    sbtCityService.saveCity(city);
                    //更新每个城市的basic信息
                    String cityCode = city.getCity();
                    int month = Integer.valueOf(city.getMonth().trim());
                    Basic basic = sheBaoTong.getBasic(cityCode);

                    LOGGER.info("社保通" + city.getCname() + "-" + month + "【" + JSON.toJSONString(basic) + "】");
                    redisTemplate.opsForValue().set(SbtService.redis_basic_cache_key + cityCode, basic);

                    SbtBasicBean sbtBasicBean = sbtBasicService.getByCityAndMonth(cityCode, month);

                    if (null == sbtBasicBean) {
                        sbtBasicService.saveBasic(cityCode, month, JSON.toJSONString(basic));
                    } else {
                        sbtBasicBean.setData(JSON.toJSONString(basic));
                        sbtBasicService.updateBasic(sbtBasicBean);
                    }
                }
            }
        }
        LOGGER.info("存储社保通数据同步完成");
    }


}
