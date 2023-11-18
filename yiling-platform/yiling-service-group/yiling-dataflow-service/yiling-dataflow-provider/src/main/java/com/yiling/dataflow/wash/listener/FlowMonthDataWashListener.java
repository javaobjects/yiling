package com.yiling.dataflow.wash.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.enums.FlowWashTaskStatusEnum;
import com.yiling.dataflow.wash.handler.FlowGoodsBatchDetailWashHandler;
import com.yiling.dataflow.wash.handler.FlowPurchaseWashHandler;
import com.yiling.dataflow.wash.handler.FlowSaleWashHandler;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/8
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_WASH_TASK, consumerGroup = Constants.TAG_FLOW_WASH_TASK,maxThread = 3)
public class FlowMonthDataWashListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private FlowMonthWashTaskService flowMonthWashTaskService;

    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;

    @Autowired
    private FlowPurchaseWashHandler flowPurchaseWashHandler;

    @Autowired
    private FlowSaleWashHandler flowSaleWashHandler;

    @Autowired
    private FlowGoodsBatchDetailWashHandler flowGoodsBatchDetailWashHandler;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg  = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        Long fmwtId = Long.parseLong(msg);

        FlowMonthWashTaskDO flowMonthWashTaskDO = flowMonthWashTaskService.getById(fmwtId);
        if (flowMonthWashTaskDO == null) {
            return MqAction.CommitMessage;
        }

        // 获取日程
        FlowMonthWashControlDO flowMonthWashControlDO = flowMonthWashControlService.getById(flowMonthWashTaskDO.getFmwcId());
        if (flowMonthWashControlDO.getTaskStatus() == 3) {    // 弃用
            // 当重新备份时，需要将状态设置为已弃用
            flowMonthWashTaskService.updateWashStatusById(flowMonthWashTaskDO.getId(), FlowWashTaskStatusEnum.DEPRECATED.getCode());
            return MqAction.CommitMessage;
        }

        if (FlowTypeEnum.PURCHASE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            flowPurchaseWashHandler.wash(fmwtId);
        } else if (FlowTypeEnum.SALE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            flowSaleWashHandler.wash(fmwtId);
        } else if (FlowTypeEnum.GOODS_BATCH.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            flowGoodsBatchDetailWashHandler.wash(fmwtId);
        }
        return MqAction.CommitMessage;
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
