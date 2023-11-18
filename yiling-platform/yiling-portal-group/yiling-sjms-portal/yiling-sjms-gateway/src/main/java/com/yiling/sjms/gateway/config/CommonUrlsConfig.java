package com.yiling.sjms.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于配置共有权限名单资源路径
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "secure.common")
public class CommonUrlsConfig {

    private List<String> urls = new ArrayList<>();

}
