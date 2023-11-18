package com.yiling.framework.common.redis.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.redis.service.impl.RedisServiceImpl;

/**
 * Redis基础配置
 */
public class BaseRedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringKeySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashKeySerializer(stringKeySerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //设置特有的Redis配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        //定制化的Cache为X 时长
        cacheConfigurations.put(RedisKey.ONE_MINUTE_PREFIX,customRedisCacheConfiguration(Duration.ofMinutes(1)));
        cacheConfigurations.put(RedisKey.FIVE_MINUTE_PREFIX,customRedisCacheConfiguration(Duration.ofMinutes(5)));
        cacheConfigurations.put(RedisKey.TEN_MINUTE_PREFIX,customRedisCacheConfiguration(Duration.ofMinutes(10)));
        cacheConfigurations.put(RedisKey.THIRTY_MINUTE_PREFIX,customRedisCacheConfiguration(Duration.ofMinutes(30)));
        cacheConfigurations.put(RedisKey.ONE_HOUR_PREFIX,customRedisCacheConfiguration(Duration.ofHours(1)));
        cacheConfigurations.put(RedisKey.FOUR_HOUR_PREFIX,customRedisCacheConfiguration(Duration.ofHours(4)));
        cacheConfigurations.put(RedisKey.SIX_HOUR_PREFIX,customRedisCacheConfiguration(Duration.ofHours(6)));
        cacheConfigurations.put(RedisKey.EIGHT_HOUR_PREFIX,customRedisCacheConfiguration(Duration.ofHours(8)));
        cacheConfigurations.put(RedisKey.TWELVE_HOUR_PREFIX,customRedisCacheConfiguration(Duration.ofHours(12)));
        cacheConfigurations.put(RedisKey.EIGHTEEN_HOUR_PREFIX,customRedisCacheConfiguration(Duration.ofHours(18)));
        cacheConfigurations.put(RedisKey.ONE_DAY_PREFIX,customRedisCacheConfiguration(Duration.ofDays(1)));

        //默认超时时间4小时
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(customRedisCacheConfiguration(Duration.ofHours(4)))
                //设置个性化的Cache配置
                .withInitialCacheConfigurations(cacheConfigurations)
                //Cache的事务支持
                .transactionAware()
                .build();
    }

    private RedisCacheConfiguration customRedisCacheConfiguration(Duration ttl) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringKeySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                //设置Cache的前缀，默认::
                .computePrefixWith(cacheName -> cacheName + ":")
                .entryTtl(ttl);
        return config;
    }

    @Bean
    public RedisSerializer stringKeySerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public RedisSerializer valueSerializer() {
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
        // 反序列化时，有些字段在实体类中找不到则忽略
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    /**
     * 自定义 KeyGenerator
     */
    @Bean(name = "simpleKeyGenerator")
    public KeyGenerator simpleKeyGenerator() {
        return (target, method, params) -> {
            Object key = SimpleKeyGenerator.generateKey(params);
            String keyStr = key.toString().replace("SimpleKey","");
            return new StringBuilder(keyStr);
        };
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuffer sb = new StringBuffer();
            sb.append(target.getClass().getName()).append(":");
            sb.append(method.getName()).append(":");
            sb.append(StringUtils.arrayToDelimitedString(params, "_"));
            return sb.toString();
        };
    }

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }

}
