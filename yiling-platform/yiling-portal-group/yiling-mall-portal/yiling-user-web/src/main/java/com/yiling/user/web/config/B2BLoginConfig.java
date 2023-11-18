package com.yiling.user.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * B2B登录配置
 *
 * @author xuan.zhou
 * @date 2022/4/7
 */
@Getter
@Setter
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "b2b.login")
public class B2BLoginConfig {

    /**
     * 允许登录的“医疗机构”类型的企业ID
     */
    private List<Long> enabledHospitalEids = new ArrayList<>();
}
