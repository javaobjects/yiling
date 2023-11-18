package com.yiling.admin.erp.redis.util;

import java.util.HashSet;
import java.util.Set;

import cn.hutool.core.util.StrUtil;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @Description: redis主从+sentinel模式部署（实现自动failover）操作工具类
 * @Author: qi.xiong
 * @Date: 2018/11/19
 */
public class JedisUtils {

    private static final String DEFAULT_MASTER_NAME = "mymaster";
    private volatile static JedisSentinelPool jedisSentinelPool;

    private JedisUtils() {}

    /**
     * 初始化连接池
     */
    private static void init() {
        final String masterName =DEFAULT_MASTER_NAME;
        String addr = "redis1.prd.yl.local:25001,redis2.prd.yl.local:25001";
        String[] nodes = addr.split(",");
        if (nodes.length == 0) {
            throw new RuntimeException("哨兵地址未配置!");
        }

        Set<String> sentinels = new HashSet<>();
        for (String node : nodes) {
            sentinels.add(HostAndPort.parseString(node).toString());
        }

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(300);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setLifo(true);

        jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig,"0PmInwufGTIV8h4PmoyG");
    }

    /**
     * 获取 JedisSentinelPool
     * @return
     */
    private static JedisSentinelPool getJedisSentinelPool() {
        // 延迟初始化并且保证只初始化一次
        if (jedisSentinelPool == null) {
            synchronized (JedisUtils.class) {
                if (jedisSentinelPool == null) {
                    init();
                }
            }
        }
        if (jedisSentinelPool == null) {
            throw new RuntimeException("初始化失败!");
        }
        return jedisSentinelPool;
    }

    /**
     * 获取 jedis
     * @return
     */
    public static Jedis getJedis() {
        return getJedisSentinelPool().getResource();
    }

}
