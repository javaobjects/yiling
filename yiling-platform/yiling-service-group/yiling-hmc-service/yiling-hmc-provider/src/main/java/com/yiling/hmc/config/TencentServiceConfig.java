package com.yiling.hmc.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯服务配置
 *
 * @author: fan.shen
 * @date: 2022/6/16
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "tencent.service")
@RefreshScope
public class TencentServiceConfig {

    /**
     * 腾讯服务URL
     */
    private String baseUrl;

    /**
     * 腾讯IM appId
     */
    private Integer tencentIMAppId;

    /**
     * 腾讯IM secretKey
     */
    private String tencentIMSecretKey;

    /**
     * 管理员
     */
    private String adminUser;


}
