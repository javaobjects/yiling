package com.yiling.open.erp.listener;

import java.text.MessageFormat;

import com.yiling.open.erp.util.ErpConstants;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpCustomerHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户信息消息消费监听
 *
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@RocketMqListener(topic = "erp_customer", consumerGroup = "erp_customer")
public class ErpCustomerMqListener implements MessageListener {

    @Autowired
    private ErpCustomerHandler erpCustomerHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            erpCustomerHandler.handleCustomerMqSync(Long.parseLong(message.getTags()), ErpTopicName.ErpCustomer.getMethod(), JSONArray.parseArray(msg, ErpCustomerDTO.class));
        } catch (Exception e) {
            log.error(MessageFormat.format(ErpConstants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
