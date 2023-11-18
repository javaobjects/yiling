package com.yiling.admin.erp.redis.factory;


import com.yiling.admin.erp.redis.service.RedisService;
import com.yiling.admin.erp.redis.service.impl.RedisSentinelServiceImpl;

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
