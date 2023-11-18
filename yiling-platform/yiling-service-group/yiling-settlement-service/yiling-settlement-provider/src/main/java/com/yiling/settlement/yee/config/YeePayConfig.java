package com.yiling.settlement.yee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 易宝支付配置信息
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "pay.yee")
public class YeePayConfig {

    /**
     * 易宝主商户号
     */
    private String merchantNo;
    /**
     * 会员支付子商户号
     */
    private String memberMerchantNo;
    /**
     * 健康管理中心子商户号
     */
    private String hmcSubMerchantNo;
    /**
     * 商户key
     */
    private String appKey;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 支付或者退款回到地址
     */
    private String notifyUrl;

    /**
     * 打款回调地址
     */
    private String businessPayNotifyUrl;

    /**
     * 支付成功后跳转地址
     */
    private String redirectUrl;

}
