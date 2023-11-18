package com.yiling.sjms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * SSO 配置类
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Getter
@Setter
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "oa.sso")
public class SsoConfig {

    /**
     * 签名秘钥
     */
    private String signSecret;

    /**
     * 是否校验签名
     */
    private Boolean verifySign;

    /**
     * 是否校验请求重放
     */
    private Boolean verifyReplay;

    /**
     * 允许请求重放时间(s)
     */
    private Integer replayTime;

    /**
     * PC端错误页面
     */
    private String pcErrorPage;

    /**
     * APP端错误页面
     */
    private String appErrorPage;

    /**
     * APP端token处理页
     */
    private String appTokenPage;
}
