package com.yiling.ih.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 互联网医院服务配置
 *
 * @author: xuan.zhou
 * @date: 2022/6/16
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "ih.service")
public class IHServiceConfig {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * APP_KEY
     */
    private String appKey;

    /**
     * APP_SECRET
     */
    private String appSecret;

    /**
     * 互联网医院服务URL
     */
    private String baseUrl;

    /**
     * 是否对请求进行签名
     */
    private Boolean sign;

    /**
     * 是否对数据包进行加密
     */
    private Boolean encrypt;
}
