package com.yiling.basic.mail.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.Getter;
import lombok.Setter;

/**
 * 邮件配置类
 *
 * @author: xuan.zhou
 * @date: 2022/11/14
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@RefreshScope
public class MailConfig {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String defaultEncoding;

    private String protocol;

    private Properties properties;

    @Bean
    @RefreshScope
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.host);
        javaMailSender.setPort(this.port);
        javaMailSender.setUsername(this.username);
        javaMailSender.setPassword(this.password);
        javaMailSender.setDefaultEncoding(this.defaultEncoding);
        javaMailSender.setProtocol(this.protocol);
        javaMailSender.setJavaMailProperties(this.properties);
        return javaMailSender;
    }
}
