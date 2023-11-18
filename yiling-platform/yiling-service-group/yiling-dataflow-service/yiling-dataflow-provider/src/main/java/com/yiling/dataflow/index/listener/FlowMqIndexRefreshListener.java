package com.yiling.dataflow.index.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.flow.enums.ErpTopicName;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息消费监听
 *
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_INDEX_REFRESH, consumerGroup = Constants.TAG_FLOW_INDEX_REFRESH)
public class FlowMqIndexRefreshListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private FlowSaleService             flowSaleService;
    @Autowired
    private FlowPurchaseService         flowPurchaseService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            UpdateFlowIndexRequest updateFlowIndexRequest = JSON.parseObject(msg, UpdateFlowIndexRequest.class);
            if (StrUtil.isEmpty(updateFlowIndexRequest.getTaskCode())) {
                if (updateFlowIndexRequest.getYear() <= 1) {
                    flowSaleService.refreshFlowSale(updateFlowIndexRequest);
                    flowPurchaseService.refreshFlowPurchase(updateFlowIndexRequest);
                    flowGoodsBatchDetailService.refreshFlowGoodsBatchDetail(updateFlowIndexRequest);
                } else {
                    flowSaleService.refreshFlowSaleBakup(updateFlowIndexRequest);
                    flowPurchaseService.refreshFlowPurchaseBackup(updateFlowIndexRequest);
                    flowGoodsBatchDetailService.refreshFlowGoodsBatchDetailBackup(updateFlowIndexRequest);
                }
            }else{
                if(updateFlowIndexRequest.getTaskCode().equals(ErpTopicName.ErpGoodsBatchFlow.getMethod())){
                    flowGoodsBatchDetailService.refreshFlowGoodsBatchDetail(updateFlowIndexRequest);
                }
            }
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
