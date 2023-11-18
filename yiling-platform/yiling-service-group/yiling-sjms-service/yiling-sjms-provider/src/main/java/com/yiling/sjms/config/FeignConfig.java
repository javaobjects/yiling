package com.yiling.sjms.config;

import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.querymap.BeanQueryMapEncoder;

/**
 * Feign Config
 *
 * @author xuan.zhou
 * @date 2023/1/6
 **/
@Configuration
public class FeignConfig {

    @Bean
    public Contract feignConfiguration() {
        return new SpringMvcContract();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().queryMapEncoder(new BeanQueryMapEncoder()).retryer(Retryer.NEVER_RETRY);
    }
}
