package com.yiling.activity.web.config;

import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yiling.framework.common.redis.config.BaseRedisConfig;

/**
 * Redis Config
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Configuration
@EnableCaching
public class RedisConfig extends BaseRedisConfig {

    @Autowired
    RedisService redisService;

    @Bean
    public WxUtils wxUtils() {
        WxUtils wxUtils = new WxUtils(redisService);
        return wxUtils;
    }
}