package com.yiling.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;
import com.yiling.framework.rocketmq.annotation.EnableRocketMq;

@SpringBootApplication(scanBasePackages = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDubbo
@EnableDiscoveryClient
@EnableRocketMq
public class UserProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
    }

}