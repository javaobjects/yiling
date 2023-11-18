package com.yiling.activity.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 微信公众号配置信息类
 * @Description
 * @Author fan.shen
 * @Date 2022/4/29
 */

@Data
public class HmcGzhItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * appId
     */
    private String appId;

    /**
     * secret
     */
    private String secret;

    /**
     * 公众号类型 test、prod
     */
    private String type;

    /**
     * 生产环境开关 1-开,回复老版客服消息，0-关，回复新版客服消息
     */
    private Integer flag;

}
