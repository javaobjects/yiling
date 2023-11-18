package com.yiling.sjms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端 配置类
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Getter
@Setter
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "client")
public class ClientConfig {

    /**
     * 客户端访问地址
     */
    private String url;

    /**
     * 客户端token配置
     */
    private Token token;

    @Getter
    @Setter
    @RefreshScope
    public static class Token {

        /**
         * token的cookie配置
         */
        private Cookie cookie;

        @Getter
        @Setter
        @RefreshScope
        public static class Cookie {

            /**
             * 名称
             */
            private String name;

            /**
             * 域名
             */
            private String domain;

            /**
             * 路径
             */
            private String path;

            /**
             * 存活时间
             */
            private Integer maxAge;
        }
    }

}
