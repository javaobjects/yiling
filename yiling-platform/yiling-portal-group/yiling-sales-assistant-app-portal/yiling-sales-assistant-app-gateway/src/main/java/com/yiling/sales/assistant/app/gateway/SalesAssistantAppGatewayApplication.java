package com.yiling.sales.assistant.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;

@SpringBootApplication(scanBasePackages = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDiscoveryClient
public class SalesAssistantAppGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesAssistantAppGatewayApplication.class, args);
    }
}
