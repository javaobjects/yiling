package com.yiling.marketing.config;

import org.springframework.cache.annotation.EnableCaching;
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
}
