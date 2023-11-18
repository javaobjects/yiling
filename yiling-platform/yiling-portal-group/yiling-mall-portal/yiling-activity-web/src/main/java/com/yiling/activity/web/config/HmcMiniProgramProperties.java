package com.yiling.activity.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信公众号配置类
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "hmc.mini-program")
public class HmcMiniProgramProperties {

    private String appId;

    private String secret;

}
