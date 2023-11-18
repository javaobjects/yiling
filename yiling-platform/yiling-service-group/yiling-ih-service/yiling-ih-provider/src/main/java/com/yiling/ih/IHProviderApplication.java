package com.yiling.ih;

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
 * 互联网医院 服务
 *
 * @author xuan.zhou
 * @date 2022/6/6
 */
@SpringBootApplication(scanBasePackages = { "com.yiling" })
@ComponentScan(value = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableFeignClients(basePackages = "com.yiling.ih.*.feign")
@EnableDubbo
@EnableDiscoveryClient
@EnableRocketMq
public class IHProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(IHProviderApplication.class, args);
    }
}
