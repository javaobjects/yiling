package com.yiling.open.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;

/**
 * 开放平台网关
 *
 * @author xuan.zhou
 * @date 2022/6/1
 **/
@SpringBootApplication(scanBasePackages = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDiscoveryClient
public class OpenGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenGatewayApplication.class, args);
    }
}
