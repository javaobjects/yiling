package com.yiling.hmc.config;

import com.yiling.hmc.tencent.interceptor.TencentRequestInterceptor;
import feign.*;
import feign.querymap.BeanQueryMapEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new TencentRequestInterceptor();
    }
}
