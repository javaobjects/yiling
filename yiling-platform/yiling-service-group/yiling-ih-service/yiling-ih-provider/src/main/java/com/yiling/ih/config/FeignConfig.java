package com.yiling.ih.config;

import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yiling.ih.feign.interceptor.SignRequestInterceptor;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.querymap.BeanQueryMapEncoder;


/**
 * feign配置
 * @author gxl
 */
@Configuration
public class FeignConfig {
    @Bean
    public Contract feignConfiguration() {
        return new SpringMvcContract();
    }
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    /**
     * 没有此配置 @SpringQueryMap  参数实体类继承的父类无法映射
     * @return
     */
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .queryMapEncoder(new BeanQueryMapEncoder())
                .retryer(Retryer.NEVER_RETRY);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new SignRequestInterceptor();
    }
}
