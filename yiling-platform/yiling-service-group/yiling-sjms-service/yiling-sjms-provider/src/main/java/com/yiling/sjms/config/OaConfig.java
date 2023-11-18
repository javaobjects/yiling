package com.yiling.sjms.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * OA 配置类
 *
 * @author: xuan.zhou
 * @date: 2023/4/13
 */
@Getter
@Setter
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "oa")
public class OaConfig {

    private App app;

    @Getter
    @Setter
    @RefreshScope
    public static class App {

        private List<Integer> todoFormTypeCodes = new ArrayList<>();
    }
}
