package com.yiling.open.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 开放平台服务配置
 *
 * @author xuan.zhou
 * @date 2022/6/16
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "service")
public class ServiceConfig {

    /**
     * 是否校验请求签名
     */
    private Boolean verifySign;

    /**
     * 是否解密数据包
     */
    private Boolean decryptBody;

    /**
     * 是否校验请求重放
     */
    private Boolean verifyReplay;

    /**
     * 允许请求重放时间
     */
    private Integer replayTime;

    private List<AppInfo> appInfoList = new ArrayList<>();

    public AppInfo getAppInfo(String appKey) {
        AppInfo appInfo = appInfoList.stream().filter(e -> e.getAppKey().equals(appKey)).findFirst().orElse(null);
        return appInfo;
    }

    @Getter
    @Setter
    @ToString
    public static class AppInfo implements java.io.Serializable {

        private static final long serialVersionUID = 2389344647783696362L;

        private String appId;

        private String appKey;

        private String appSecret;
    }
}
