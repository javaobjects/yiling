package com.yiling.hmc.config;

import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yiling.framework.common.redis.config.BaseRedisConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

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
    RedisMessageListenerContainer listenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        return listenerContainer;
    }

    @Bean
    KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        return new RedisKeyExpirationListener(listenerContainer);
    }

    @Bean
    public WxUtils wxUtils() {
        WxUtils wxUtils = new WxUtils(redisService);
        return wxUtils;
    }

}
