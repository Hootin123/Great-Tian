package com.xtr.core.service.shebao;

import com.xtr.BaseTest;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.comm.sbt.api.City;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author Xuewu
 * @Date 2016/9/29.
 */
public class CacheTest extends BaseTest {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    SbtService sbtService;

//    @Test
//    public void test() {
//        redisTemplate.opsForValue().set("citieskey", 123);
//
//        System.out.println(redisTemplate.opsForValue().get("citieskey"));
//        Set keys = redisTemplate.keys("citiesk*");
//        System.out.println(keys);
//    }


//    @Test
//    public void test2() {
//        List<City> cities = sbtService.getCities();
//        System.out.println(cities);
//        cities = sbtService.getCities();
//        System.out.println(cities);
//    }
}
