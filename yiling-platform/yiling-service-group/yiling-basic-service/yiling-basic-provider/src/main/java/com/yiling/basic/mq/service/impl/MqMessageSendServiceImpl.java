package com.yiling.basic.mq.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;
import com.yiling.basic.mq.entity.MqMessageSendingDO;
import com.yiling.basic.mq.service.MqMessageSendService;
import com.yiling.basic.mq.service.MqMessageSendingService;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.framework.rocketmq.exception.MqContextException;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * MQ 消息发送服务
 *
 * @author: xuan.zhou
 * @date: 2022/1/12
 */
@Slf4j
@Service
@RefreshScope
public class MqMessageSendServiceImpl implements MqMessageSendService {

    @Value("${mq.sender.retryInterval}")
    private Integer retryInterval;
    @Value("${mq.sender.retryTimes}")
    private Integer retryTimes;

    @Autowired
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    private MqMessageSendingService mqMessageSendingService;

    @Override
    public MqMessageBO prepare(MqMessageBO mqMessageBO) {
        MqMessageSendingDO entity = new MqMessageSendingDO();
        entity.setTraceId(mqMessageBO.getTraceId());
        entity.setTopic(mqMessageBO.getTopic());
        entity.setTags(mqMessageBO.getTags());
        entity.setKeys(mqMessageBO.getKeys());
        entity.setBody(mqMessageBO.getBody());
        MqDelayLevel delayLevel = mqMessageBO.getDelayLevel();
        if (delayLevel != null) {
            entity.setDelayLevel(delayLevel.getLevel());
        }
        entity.setOrderId(mqMessageBO.getOrderId());
        entity.setNextRetryTime(DateUtil.offset(new Date(), DateField.SECOND, retryInterval));
        mqMessageSendingService.save(entity);

        // 设置ID并返回
        mqMessageBO.setId(entity.getId());
        return mqMessageBO;
    }

    @Override
    public void send(MqMessageBO mqMessageBO) {
        Long id = mqMessageBO.getId();
        String traceId = mqMessageBO.getTraceId();
        String topic = mqMessageBO.getTopic();
        String tags = mqMessageBO.getTags();
        String keys = mqMessageBO.getKeys();
        String body = mqMessageBO.getBody();
        MqDelayLevel delayLevel = mqMessageBO.getDelayLevel();
        Integer orderId = mqMessageBO.getOrderId();

        try {
            if (delayLevel != null) {
                rocketMqProducerService.sendDelay(topic, tags, keys, body, delayLevel, new RocketSendCallback(id, traceId));
            } else if (orderId != null && orderId != 0) {
                rocketMqProducerService.sendOrderly(topic, tags, keys, body, orderId, new RocketSendCallback(id, traceId));
            } else {
                rocketMqProducerService.sendAsync(topic, tags, keys, body, new RocketSendCallback(id, traceId));
            }
            log.info("消息发送成功：{}", JSONUtil.toJsonStr(mqMessageBO));
        } catch (Exception e) {
            log.error("消息发送失败：{}", JSONUtil.toJsonStr(mqMessageBO), e);
        }
    }

    @Override
    public void retry(List<MqMessageSendingDTO> mqMessageSendingDTOList) {
        if (CollUtil.isEmpty(mqMessageSendingDTOList)) {
            return;
        }

        mqMessageSendingDTOList.forEach(e -> {
            if (e.getRetryTimes() >= retryTimes) {
                log.warn("消息超过最大重试次数：{}", JSONUtil.toJsonStr(e));
                mqMessageSendingService.updateFailed(e.getId(), e.getMsgId(), null);
            } else {
                log.info("准备重试发送消息：{}", JSONUtil.toJsonStr(e));
                // 更新记录重试次数
                mqMessageSendingService.updateRetryTimes(e.getId());

                // 再次发送消息
                MqMessageBO mqMessageBO = new MqMessageBO(e.getTopic(), e.getTags(), e.getBody(), e.getOrderId(), MqDelayLevel.getByLevel(e.getDelayLevel()));
                mqMessageBO.setId(e.getId());
                this.send(mqMessageBO);
            }
        });
    }

    class RocketSendCallback implements SendCallback {

        private Long id;
        private String traceId;

        public RocketSendCallback(Long id, String traceId) {
            this.id = id;
            this.traceId = traceId;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            log.info("RocketSendCallback-Success -> traceId={}, topic={}, msgId={}, id={}", traceId, sendResult.getMessageQueue().getTopic(), sendResult.getMsgId(), id);
            // 消息发送成功，删除“发送中”记录
            mqMessageSendingService.updateSuccessful(id, sendResult.getMsgId());
        }

        @Override
        public void onException(Throwable e) {
            String msgId = null;
            if (e instanceof MqContextException) {
                MqContextException context = (MqContextException) e;
                msgId = context.getMessageId();
                log.error("RocketSendCallback-MqContextException -> traceId={}, topic={}, msgId={}, id={}", traceId, context.getTopic(), msgId, id, e);
            } else {
                log.error("RocketSendCallback-Exception -> traceId={}, id={}", traceId, id, e);
            }

            // 更新发送失败信息
            mqMessageSendingService.updateFailedMsg(id, msgId, StrUtil.sub(e.getMessage(), 0, 1000));
        }
    }

}
