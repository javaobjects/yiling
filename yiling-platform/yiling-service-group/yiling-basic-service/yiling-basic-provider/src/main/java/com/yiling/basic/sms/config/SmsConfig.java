package com.yiling.basic.sms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * 短信配置类
 *
 * @author: xuan.zhou
 * @date: 2021/8/14
 */
@Getter
@RefreshScope
@Configuration
public class SmsConfig {

    @Value("${sms.sender-enabled}")
    private boolean senderEnabled;
    @Value("${sms.interfaceUrl}")
    private String smsInterfaceUrl;
    @Value("${sms.username}")
    private String smsUsername;
    @Value("${sms.password}")
    private String smsPassword;

    @Value("${sms.verifyCode.default}")
    private String verifyCodeDefault;
    @Value("${sms.verifyCode.generater-enabled}")
    private boolean verifyCodeGeneraterEnabled;
    @Value("${sms.verifyCode.expiration-time}")
    private Integer verifyCodeExpirationTime;
    @Value("${sms.verifyCode.token-expiration-time}")
    private Integer verifyCodeTokenExpirationTime;

    @Value("${sms.riskControl-enabled:true}")
    private boolean riskControlEnabled;
    @Value("${sms.verifyCode.riskControl.check-time:300}")
    private Integer verifyCodeRiskControlCheckTime;
    @Value("${sms.verifyCode.riskControl.lock-time:300}")
    private Integer verifyCodeRiskControlLockTime;
}
