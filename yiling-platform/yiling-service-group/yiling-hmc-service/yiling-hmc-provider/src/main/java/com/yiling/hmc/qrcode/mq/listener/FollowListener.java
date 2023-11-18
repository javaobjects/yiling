package com.yiling.hmc.qrcode.mq.listener;

import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.hmc.wechat.enums.SourceEnum;
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

import java.util.Objects;

/**
 * 关注公众号消息
 *
 * @author: gxl
 * @date: 2022/4/25
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_GZH_SUBSCRIBE, consumerGroup = Constants.TOPIC_HMC_GZH_SUBSCRIBE)
public class FollowListener extends AbstractMessageListener {

    @DubboReference
    QrcodeStatisticsApi qrcodeStatisticsApi;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    GzhUserApi gzhUserApi;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[FollowListener]关注公众号消息参数：{},消息：{}", body, message.getMsgId());
        GzhUserDTO userDTO = gzhUserApi.getById(Long.valueOf(body));
        if (Objects.isNull(userDTO)) {
            log.info("[FollowListener]根据参数未获取到用户信息,用户id：{}", body);
            return MqAction.CommitMessage;
        }
        if (!SourceEnum.BOX_CODE.getType().equals(userDTO.getSubscribeSource())) {
            log.info("[FollowListener]用户非扫药盒二维码关注，跳过统计：{}", userDTO);
            return MqAction.CommitMessage;
        }
        SaveQrcodeStatisticsRequest request = new SaveQrcodeStatisticsRequest();
        request.setFollow(1);
        qrcodeStatisticsApi.save(request);
        log.info("[FollowListener]订阅公众号保存二维码统计数据完成，参数:{}", request);
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