package com.yiling.admin.cms.content.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 业务线对应板块配置类
 * fan.shen
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "business-line")
public class CmsBusinessLineConfig {

    /**
     * 业务线对应模块配置关系
     * 1、2C用户侧
     *      健康资讯
     * 2、医生侧
     *      行业资讯
     *      络病学院
     * ...
     */
    private Map<Long, List<ModulesConfig>> modules;

}
