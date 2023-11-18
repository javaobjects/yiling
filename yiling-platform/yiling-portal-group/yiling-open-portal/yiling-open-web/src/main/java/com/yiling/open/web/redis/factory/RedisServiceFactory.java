package com.yiling.open.web.redis.factory;

import com.yiling.open.web.redis.service.RedisService;
import com.yiling.open.web.redis.service.impl.RedisSentinelServiceImpl;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/12/5
 */
public class RedisServiceFactory {

    public static RedisService getRedisService() {
        return new RedisSentinelServiceImpl();
    }

}
