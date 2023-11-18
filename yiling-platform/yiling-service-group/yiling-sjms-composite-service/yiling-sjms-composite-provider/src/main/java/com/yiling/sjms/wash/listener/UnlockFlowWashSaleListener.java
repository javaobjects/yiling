package com.yiling.sjms.wash.listener;

import java.text.MessageFormat;

import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.order.util.Constants;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.gb.handler.RelationShipNotificationHandler;
import com.yiling.sjms.wash.handler.UnFlowWashHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import static com.yiling.framework.common.util.Constants.TAG_UNLOCK_FLOW_SALE_MATCH_TASK;
import static com.yiling.framework.common.util.Constants.TAG_UPDATE_CRM_RELATIONSHIP;
import static com.yiling.framework.common.util.Constants.TOPIC_UNLOCK_FLOW_SALE_MATCH_TASK;
import static com.yiling.framework.common.util.Constants.TOPIC_UPDATE_CRM_RELATIONSHIP;

/**
 * 非锁销售分配mq消费消息
 *
 * @author: shixing.sun
 * @date: 2022/6/8
 */
@Slf4j
@RocketMqListener(topic = TOPIC_UNLOCK_FLOW_SALE_MATCH_TASK, consumerGroup = TAG_UNLOCK_FLOW_SALE_MATCH_TASK,maxThread = 1)
public class UnlockFlowWashSaleListener implements MessageListener {

    @Autowired
    private UnFlowWashHandler unFlowWashHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (StringUtils.isBlank(msg)) {
            log.error("消息为空");
            return MqAction.CommitMessage;
        }
        //gb_order表里面id
        Long id = Long.parseLong(msg);
        unFlowWashHandler.wash(id);
        return MqAction.CommitMessage;
    }

}
