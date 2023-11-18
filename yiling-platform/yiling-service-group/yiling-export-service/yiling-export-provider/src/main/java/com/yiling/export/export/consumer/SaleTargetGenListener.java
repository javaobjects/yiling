package com.yiling.export.export.consumer;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.sale.dto.request.GenerateCrmConfigMouldRequest;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.export.export.service.impl.TargetResolveDetailExportImpl;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售指标分解模板生成
 * @author: gxl
 * @date: 2023/5/11
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GEN_SALE_TARGET_TEMPLATE, consumerGroup = Constants.TOPIC_GEN_SALE_TARGET_TEMPLATE,tag = Constants.TAG_GEN_SALE_TARGET_TEMPLATE)
public class SaleTargetGenListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private TargetResolveDetailExportImpl targetResolveDetailExport;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
        GenerateCrmConfigMouldRequest request = JSONObject.parseObject(body, GenerateCrmConfigMouldRequest.class);
        QueryResolveDetailPageRequest resolveDetailPageRequest = new QueryResolveDetailPageRequest();
        resolveDetailPageRequest.setSaleTargetId(request.getSaleTargetId()).setDepartId(request.getDepartId());
        targetResolveDetailExport.genTemplate(resolveDetailPageRequest);
        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 0;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}