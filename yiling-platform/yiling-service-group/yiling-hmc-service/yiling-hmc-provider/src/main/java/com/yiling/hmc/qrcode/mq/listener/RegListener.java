package com.yiling.hmc.qrcode.mq.listener;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.yiling.hmc.wechat.enums.SourceEnum;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.hmc.qrcode.api.QrcodeStatisticsApi;
import com.yiling.hmc.qrcode.dto.request.SaveQrcodeStatisticsRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * 注册消息
 * @author: gxl
 * @date: 2022/4/25
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_REG_MINI_PROGRAM, consumerGroup = Constants.TAG_HMC_REG_MINI_PROGRAM)
public class RegListener extends AbstractMessageListener {
    @DubboReference
    QrcodeStatisticsApi qrcodeStatisticsApi;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @Autowired
    WxMaService wxMaService;


    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[RegListener]小程序注册消息参数：{},消息id：{}",body, message.getMsgId());
        HmcUser hmcUser = hmcUserApi.getByIdAndAppId(Long.valueOf(body), wxMaService.getWxMaConfig().getAppid());
        if (Objects.isNull(hmcUser)) {
            log.info("[RegListener]根据参数未获取到用户信息,用户id：{}", body);
            return MqAction.CommitMessage;
        }
        if (!SourceEnum.BOX_CODE.getType().equals(hmcUser.getRegisterSource())) {
            log.info("[RegListener]用户非扫药盒二维码注册，跳过统计：{}", hmcUser);
            return MqAction.CommitMessage;
        }
        SaveQrcodeStatisticsRequest request = new SaveQrcodeStatisticsRequest();
        request.setRegister(1);
        qrcodeStatisticsApi.save(request);
        log.info("[RegListener]小程序注册保存药盒二维码统计数据完成，参数:{}", request);
        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 1;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
        };
    }
}