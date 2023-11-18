package com.yiling.dataflow.relation.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据流向销售、采购、库存同步，新增或更新以岭品映射关系的通知监听
 *
 * @author: houjie.sun
 * @date: 2022/11/16
 */
@Slf4j
@RocketMqListener(topic = "topic_erp_flow_goods_relation_saveOrUpdate", consumerGroup = "topic_erp_flow_goods_relation_saveOrUpdate")
public class FlowGoodsRelationSaveOrUpdateByFlowMqListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.debug("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            QueryFlowGoodsRelationYlGoodsIdRequest ylGoodsIdRequest = JSON.parseObject(msg, QueryFlowGoodsRelationYlGoodsIdRequest.class);
            flowGoodsRelationService.saveOrUpdateByFlowSync(ylGoodsIdRequest);
            return MqAction.CommitMessage;
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}
