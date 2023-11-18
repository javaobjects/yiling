package com.yiling.payment.channel.service.support.config;

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

    /**
     * 用户appId
     */
    private String appId;

    /**
     * 请求路径地址信息
     */
    private String apiUrlAddress;

    /**
     * 微信支付商户号
     */
    private String merPtcId;

    /**
     * 支付或者退款回到地址
     */
    private String notifyUrl;

    /**
     * 支付成功后跳转地址
     */
    private String redirectUrl;

}
