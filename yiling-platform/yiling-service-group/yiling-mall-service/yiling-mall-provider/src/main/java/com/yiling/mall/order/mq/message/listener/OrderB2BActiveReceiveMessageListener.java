package com.yiling.mall.order.mq.message.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqOrderListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageOrderListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.order.api.OrderProcessApi;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: wei.wang
 * @date: 2022/08/31
 */
@Slf4j
@RocketMqOrderListener(topic = Constants.TOPIC_B2B_ORDER_ACTIVE_RECEIVE, consumerGroup = Constants.TAG_B2B_ORDER_ACTIVE_RECEIVE)
public class OrderB2BActiveReceiveMessageListener implements MessageOrderListener {

    @DubboReference
    OrderProcessApi orderProcessApi;


    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeOrderlyContext context) {
        String orderId = null;
        try {
            orderId = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderId);
            Long id = Long.valueOf(orderId);
            Boolean result = orderProcessApi.activeB2BReceiveByOrderId(id);

        } catch (BusinessException e) {

            log.debug("[{}],errorMsg:{}", orderId, e.getMessage());

            return MqAction.CommitMessage;

        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderId, e);
            throw e;
        }
        return MqAction.CommitMessage;

    }

}
