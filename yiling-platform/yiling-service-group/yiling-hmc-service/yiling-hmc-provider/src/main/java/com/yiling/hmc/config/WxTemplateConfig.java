package com.yiling.hmc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 模板消息配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "hmc-template")
public class WxTemplateConfig {

    /**
     * 用药指导模板
     */
    private String medInstruction;

    /**
     * 保单过期
     */
    private String policyExpired;

    /**
     * 取药提醒
     */
    private String takeMedReminder;

    /**
     * 用药管理 - 用药提醒
     */
    private String medsReminder;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    private String miniProgramState;

    /**
     * 中秋节推送活动消息模板id
     */
    private String midAutumnFestivalTemplateId;

    /**
     * 八子补肾发货通知消息模板id
     */
    private String baZiBuShenDeliverMsgTemplate;

    /**
     * 父亲节活动通知消息模板id
     */
    private String fatherFestivalMsgTemplate;

    /**
     * 目标对象id
     */
    private String userIdList;

}
