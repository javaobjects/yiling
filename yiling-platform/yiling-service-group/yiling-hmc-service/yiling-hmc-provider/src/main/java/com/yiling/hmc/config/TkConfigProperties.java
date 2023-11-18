package com.yiling.hmc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 泰康配置信息
 * @Description
 * @Author fan.shen
 * @Date 2022/6/30
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "tk-config")
public class TkConfigProperties {

    /**
     * appId
     */
    private String appId;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * paramsKey
     */
    private String paramsKey;

    /**
     * 泰康接口服务地址
     */
    private String tkServerUrl;

    /**
     * 服务入口渠道编码
     */
    private String sourceChannelCode;

    /**
     * 服务入口渠道名称
     */
    private String sourceChannelName;

    /**
     * 服务入口渠道编码
     */
    private String channelCode;

}
