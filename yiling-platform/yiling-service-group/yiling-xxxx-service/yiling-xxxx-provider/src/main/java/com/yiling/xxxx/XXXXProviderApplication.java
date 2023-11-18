package com.yiling.xxxx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;

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
public class XXXXProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(XXXXProviderApplication.class, args);
    }
}
