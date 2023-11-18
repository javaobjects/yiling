package com.yiling.basic.sms.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.basic.sms.config.SmsConfig;
import com.yiling.basic.sms.entity.SmsRecordDO;
import com.yiling.basic.sms.enums.SmsStatusEnum;
import com.yiling.basic.sms.service.SmsRecordService;
import com.yiling.basic.sms.service.TxSmsChannelService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.pojo.ServiceResult;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信消费者
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_SMS_CREATED, consumerGroup = Constants.TOPIC_SMS_CREATED)
public class SmsMessageListener extends AbstractMessageListener {

    @Autowired
    SmsConfig smsConfig;
    @Autowired
    SmsRecordService smsRecordService;
    @Autowired
    TxSmsChannelService txSmsChannelService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        // 短信记录ID
        Long id = Convert.toLong(body);
        SmsRecordDO smsRecordDO = smsRecordService.getById(id);
        log.info("待发送短信内容：{}", JSONUtil.toJsonStr(smsRecordDO));

        if (smsConfig.isSenderEnabled()) {
            ServiceResult result = txSmsChannelService.sendSms(smsRecordDO.getMobile(), smsRecordDO.getContent(), smsRecordDO.getSignature());
            if (!result.isSuccess()) {
                log.error("短信发送失败 -> {}", result.getMessage());
                // 更新短信记录状态
                smsRecordService.updateStatus(id, SmsStatusEnum.FAILED, result.getMessage());
            } else {
                log.info("短信发送成功");
                smsRecordService.updateStatus(id, SmsStatusEnum.SUCCESS, null);
            }
        } else {
            log.warn("发送短信功能已关闭");
            // 更新短信记录状态
            smsRecordService.updateStatus(id, SmsStatusEnum.SUCCESS, "发送短信功能已关闭，未真实发送");
        }

        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            Long id = Convert.toLong(body);
            smsRecordService.updateStatus(id, SmsStatusEnum.FAILED, e.getMessage());
        };
    }
}
