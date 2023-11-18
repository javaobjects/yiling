package com.yiling.activity.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * 健康管理中心-微信公众号配置类
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "hmc")
@Component
public class HmcGzhProperties implements Serializable {

    private List<HmcGzhItem> gzhList;

    /**
     * 根据appId获取公众号配置信息
     *
     * @param appId
     * @return
     */
    public HmcGzhItem getGzhByAppId(String appId) {
        Optional<HmcGzhItem> first = gzhList.stream().filter(item -> appId.equals(item.getAppId())).findFirst();
        return first.get();
    }

}
