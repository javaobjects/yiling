package com.yiling.open.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库连接信息
 *
 * @author: houjie.sun
 * @date: 2022/7/12
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceConfig{

    private String url;

    private String username;

    private String password;

}
