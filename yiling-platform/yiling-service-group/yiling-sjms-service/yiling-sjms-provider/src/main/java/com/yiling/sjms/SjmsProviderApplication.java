package com.yiling.sjms;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;
import com.yiling.framework.rocketmq.annotation.EnableRocketMq;

/**
 * XXXX 服务
 *
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@SpringBootApplication(scanBasePackages = { "com.yiling" })
@ComponentScan(value = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDubbo
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yiling.sjms.**.feign")
@EnableRocketMq
public class SjmsProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SjmsProviderApplication.class, args);
    }
}