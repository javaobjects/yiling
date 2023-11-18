package com.yiling.sjms.gb.listener;

import java.text.MessageFormat;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.dataflow.order.util.Constants;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.gb.handler.RelationShipNotificationHandler;

import lombok.extern.slf4j.Slf4j;

import static com.yiling.framework.common.util.Constants.TAG_UPDATE_CRM_RELATIONSHIP;
import static com.yiling.framework.common.util.Constants.TOPIC_UPDATE_CRM_RELATIONSHIP;

/**
 * 三者关系备份以后更新备份表内容，方便接口调用
 *
 * @author: shixing.sun
 * @date: 2022/6/8
 */
@Slf4j
//@RocketMqListener(topic = TOPIC_UPDATE_CRM_RELATIONSHIP, consumerGroup = TAG_UPDATE_CRM_RELATIONSHIP)
public class GbOrgManagerMqListener implements MessageListener {

    @Autowired
    private RelationShipNotificationHandler relationShipNotificationHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("接收到三者关系更新mq" + msg);
            log.info(MessageFormat.format(Constants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            /*Long flowGoodsRelationId = Long.parseLong(message.getTags());
            JSONObject jsonObject = (JSONObject) JSONObject.parse(msg);*/
            Integer offset = Integer.parseInt(msg);
            Boolean result = relationShipNotificationHandler.crmEnterpriseRelationShipBackup(offset);
            if (result) {
                log.info(MessageFormat.format(Constants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
                return MqAction.CommitMessage;
            } else{
                log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
                return MqAction.ReconsumeLater;
            }
        } catch (Exception e) {
            log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
    }

}
