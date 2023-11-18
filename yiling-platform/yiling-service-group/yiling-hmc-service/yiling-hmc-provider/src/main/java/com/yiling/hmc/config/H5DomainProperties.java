package com.yiling.hmc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * H5域名链接配置类
 * @Description
 * @Author fan.shen
 * @Date 2022/5/6
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "h5")
public class H5DomainProperties {

    private String domainUrl;

}
