package com.yiling.payment.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 交通银行支付配置信息
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "pay.bocom")
public class BocomPayConfig {
    /**
     * 私钥信息
     */
    private String privateKey;

    /**
     *  公钥信息
     */
    private String publicKey;

}
