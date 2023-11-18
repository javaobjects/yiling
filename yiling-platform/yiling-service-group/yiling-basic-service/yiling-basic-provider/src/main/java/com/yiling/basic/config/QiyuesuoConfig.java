package com.yiling.basic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.qiyuesuo.sdk.SDKClient;
import net.qiyuesuo.sdk.api.ContractService;
import net.qiyuesuo.sdk.impl.ContractServiceImpl;

/**
 * @author fucheng.bai
 * @date 2022/11/11
 */
@Configuration
public class QiyuesuoConfig {

    @Value("${qiyuesuo.open.url:http://222.223.229.43:9182}")
    private String url;

    @Value("${qiyuesuo.open.accessKey:GJOrXo0qrI}")
    private String accessKey;

    @Value("${qiyuesuo.open.accessSecret:9wOHAGfhPtyhVZM88rLshpZaI9nNTH}")
    private String accessSecret;


    @Bean
    public SDKClient getClient() {
        SDKClient client = new SDKClient(url, accessKey, accessSecret);
        return client;
    }

    @Bean
    public ContractService contractService(SDKClient client) {
        return new ContractServiceImpl(client);
    }
}
