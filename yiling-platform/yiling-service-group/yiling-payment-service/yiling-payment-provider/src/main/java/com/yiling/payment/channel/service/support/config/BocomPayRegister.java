package com.yiling.payment.channel.service.support.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bocom.api.DefaultBocomClient;
import com.yiling.framework.common.util.Constants;

/**  交通银行sdkClient
 * @author zhigang.guo
 * @date: 2023/5/8
 */
@Configuration
@ConditionalOnProperty(prefix = "pay.bocom", name = "appId", matchIfMissing = false)
public class BocomPayRegister {

    @Autowired
    private BocomPayConfig bocomPayConfig;

    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public DefaultBocomClient bocomClientService() {

        DefaultBocomClient client = new DefaultBocomClient(bocomPayConfig.getAppId(), bocomPayConfig.getPrivateKey(), bocomPayConfig.getPublicKey());

        //  测试环境可以忽略SSL证书告警，生产环境不可忽略
        if (Constants.DEBUG_ENV_LIST.contains(env)) {

            client.ignoreSSLHostnameVerifier();
        }

        return client;
    }
}
