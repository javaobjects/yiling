package com.yiling.dataflow.relation.listener;

import java.text.MessageFormat;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.dataflow.relation.handler.GoodsAuditApprovedHandler;
import com.yiling.dataflow.order.util.Constants;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品审核通过监听
 *
 * @author: houjie.sun
 * @date: 2022/6/13
 */
@Slf4j
@RocketMqListener(topic = "topic_goods_audit_erp_flow_send", consumerGroup = "topic_goods_audit_erp_flow_send")
public class GoodsAuditApprovedMqListener implements MessageListener {

    @Autowired
    private GoodsAuditApprovedHandler goodsAuditApprovedHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info(MessageFormat.format(Constants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            JSONObject jsonObject = (JSONObject) JSONObject.parse(msg);
            if (ObjectUtil.isNull(jsonObject)) {
                log.warn("FlowGoodsAuditApprovedMqListener, msg is null");
                return MqAction.CommitMessage;
            }
            Long goodsId = jsonObject.getLongValue("id");
            String goodsName = jsonObject.getString("name");
            String specifications = jsonObject.getString("specifications");
            String ename = jsonObject.getString("ename");
            Boolean result = goodsAuditApprovedHandler.handlerGoodsAuditApprovedMqSync(goodsId, goodsName, specifications, ename);
            if (!result) {
                log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
                return MqAction.ReconsumeLater;
            }
        } catch (Exception e) {
            log.info(MessageFormat.format(Constants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
