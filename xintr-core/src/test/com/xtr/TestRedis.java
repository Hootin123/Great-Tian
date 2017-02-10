package com.xtr;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/9/14 14:29
 */
public class TestRedis {
    public static void main(String args[]) {
        JedisPoolConfig config = new JedisPoolConfig();
//最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
        config.setMaxIdle(200);
//最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
        config.setMaxTotal(300);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        String host = "121.40.173.135";
        String password = "xtrRds123";
        JedisPool pool = new JedisPool(config, host, 6378, 3000, password);
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
/// ... do stuff here ... for example
            jedis.set("foo", "bar");
            String foobar = jedis.get("foo");
            System.out.println(foobar);
            jedis.zadd("sose", 0, "car");
            jedis.zadd("sose", 0, "bike");
            Set<String> sose = jedis.zrange("sose", 0, -1);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
/// ... when closing your application:
        pool.destroy();
    }
}
