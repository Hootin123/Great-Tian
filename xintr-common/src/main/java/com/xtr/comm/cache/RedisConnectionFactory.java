package com.xtr.comm.cache;

import com.xtr.comm.util.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * <p>redis连接工厂</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/14 11:08
 */
public class RedisConnectionFactory extends JedisConnectionFactory {

    /**
     * 初始化redis连接信息
     */
    public RedisConnectionFactory() {
        //是否测试环境
        String istest = PropertyUtils.getString("environ.redis.local");
        if (StringUtils.equals(istest, "1")) {
            setHostName(PropertyUtils.getString("redis.host"));
            setPort(PropertyUtils.getIntValue("redis.port", 6379));
            setTimeout(PropertyUtils.getIntValue("redis.timeout", 120000));
            if (!com.xtr.comm.jd.util.StringUtils.isBlank(PropertyUtils.getString("redis.password"))){
                setPassword(PropertyUtils.getString("redis.password"));
            }
//            setPassword("password");
        } else {
            setHostName(PropertyUtils.getString("ali.redis.host"));
            setPort(PropertyUtils.getIntValue("ali.redis.port", 6379));
            setTimeout(PropertyUtils.getIntValue("ali.redis.timeout", 120000));
            setPassword(PropertyUtils.getString("ali.redis.password"));
        }

    }
}
