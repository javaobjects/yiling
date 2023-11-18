package com.yiling.dataflow.relation.listener;

import java.text.MessageFormat;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.dataflow.order.util.Constants;
import com.yiling.dataflow.relation.handler.FlowGoodsRelationHandler;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家品与以岭品映射关系变更通知监听
 *
 * @author: houjie.sun
 * @date: 2022/6/8
 */
@Slf4j
@RocketMqListener(topic = "topic_erp_flow_goods_relation_dataflow", consumerGroup = "topic_erp_flow_goods_relation_dataflow")
public class FlowGoodsRelationMqListener implements MessageListener {

    @Autowired
    private FlowGoodsRelationHandler flowGoodsRelationHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info(MessageFormat.format(Constants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            Long flowGoodsRelationId = Long.parseLong(message.getTags());
            JSONObject jsonObject = (JSONObject) JSONObject.parse(msg);
            if (ObjectUtil.isNull(flowGoodsRelationId)) {
                log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
                return MqAction.CommitMessage;
            }
            Long oldYlGoodsId = 0L;
            Long opUserId = 0L;
            if (ObjectUtil.isNotNull(jsonObject)) {
                oldYlGoodsId = jsonObject.getLongValue("oldYlGoodsId");
                opUserId = jsonObject.getLongValue("opUserId");
            }
            int result = flowGoodsRelationHandler.handleFlowGoodsRelationMqSync(flowGoodsRelationId, opUserId);
            if (result == -1) {
                log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
                return MqAction.CommitMessage;
            } else if (result == 0){
                log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
                log.info(message.getMsgId() + ", {}", "以岭品修改存在未执行完成的任务，消息重试");
                return MqAction.ReconsumeLater;
            } else if (result == 1){
                return MqAction.CommitMessage;
            }
        } catch (Exception e) {
            log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
