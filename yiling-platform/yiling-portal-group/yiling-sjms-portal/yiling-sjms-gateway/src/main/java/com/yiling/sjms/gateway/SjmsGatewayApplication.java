package com.yiling.sjms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;

@SpringBootApplication(scanBasePackages = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDiscoveryClient
public class SjmsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SjmsGatewayApplication.class, args);
    }
}
