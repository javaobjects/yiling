package com.yiling.hmc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云实名认证校验配置类
 *
 * @Description
 * @Author fan.shen
 * @Date 2023/5/6
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "ali-real-name-authentic")
public class AliRealNameAuthenticationWebProperties {

    /**
     * url
     */
    private String url;

    /**
     * appcode
     */
    private String appcode;

}
