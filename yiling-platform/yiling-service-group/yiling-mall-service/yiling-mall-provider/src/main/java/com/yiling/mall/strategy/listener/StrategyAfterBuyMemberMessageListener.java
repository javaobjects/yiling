package com.yiling.mall.strategy.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.marketing.strategy.api.StrategyActivityApi;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 购买会员后处理策略满赠活动
 *
 * @author: yong.zhang
 * @date: 2022/9/18
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS_STRATEGY_SEND, consumerGroup = Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS_STRATEGY_SEND)
public class StrategyAfterBuyMemberMessageListener implements MessageListener {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String orderNo = null;
        try {
            orderNo = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);
            if (StrUtil.isBlank(orderNo)) {
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{orderNo为空}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);
                return MqAction.CommitMessage;
            }
            Boolean isSuccess = strategyActivityApi.sendGiftAfterBuyMember(orderNo);
            if (!isSuccess) {
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isSuccess=False}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);
                return MqAction.ReconsumeLater;
            }
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
