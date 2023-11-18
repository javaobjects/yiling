package com.yiling.sales.assistant;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;
import com.yiling.framework.rocketmq.annotation.EnableRocketMq;

/**
 * 销售助手服务
 *
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@SpringBootApplication(scanBasePackages = { "com.yiling","cn.hutool.extra.spring" })
@Import({ EncrytablePropertyConfig.class })
@EnableDubbo
@EnableDiscoveryClient
@EnableRocketMq
public class SalesAssistantProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesAssistantProviderApplication.class, args);
    }
}
