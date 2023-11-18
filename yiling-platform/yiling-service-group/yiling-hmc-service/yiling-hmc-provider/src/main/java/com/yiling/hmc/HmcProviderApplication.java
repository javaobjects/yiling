package com.yiling.hmc;

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
 * 健康管理中心服务
 *
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@SpringBootApplication(scanBasePackages = { "com.yiling","cn.hutool.extra.spring" })
@Import({ EncrytablePropertyConfig.class })
@ComponentScan(value = { "com.yiling" })
@EnableFeignClients(basePackages = "com.yiling.hmc.*.feign")
@EnableDubbo
@EnableDiscoveryClient
@EnableRocketMq
public class HmcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmcProviderApplication.class, args);
    }
}
