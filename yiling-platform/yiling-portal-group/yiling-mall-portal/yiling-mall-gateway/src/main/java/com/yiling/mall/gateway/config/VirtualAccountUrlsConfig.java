package com.yiling.mall.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于配置虚拟账号可访问资源路径
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "secure.virtual-account")
public class VirtualAccountUrlsConfig {

    private List<String> urls = new ArrayList<>();

}
