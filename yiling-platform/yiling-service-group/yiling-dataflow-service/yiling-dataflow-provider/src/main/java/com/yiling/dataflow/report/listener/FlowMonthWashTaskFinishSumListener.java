package com.yiling.dataflow.report.listener;

import java.util.Map;
import java.util.function.Consumer;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.report.dto.request.CreateFlowWashReportRequest;
import com.yiling.dataflow.report.handler.FlowWashHospitalStockReportHandler;
import com.yiling.dataflow.report.handler.FlowWashPharmacyPurchaseReportHandler;
import com.yiling.dataflow.report.handler.FlowWashSupplyStockReportHandler;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import lombok.extern.slf4j.Slf4j;

/** 清洗任务完成统计通知消息
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_WASH_TASK_FINISH_SUM_NOTIFY, consumerGroup = Constants.TAG_WASH_TASK_FINISH_SUM_NOTIFY,maxThread = 5)
public class FlowMonthWashTaskFinishSumListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    private FlowWashSupplyStockReportHandler flowWashSupplyStockReportHandler;
    @Autowired
    private FlowWashHospitalStockReportHandler flowWashHospitalStockReportHandler;
    @Autowired
    private FlowWashPharmacyPurchaseReportHandler flowWashPharmacyPurchaseReportHandler;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (msg == null) {
            log.info("msg 为空!");
            return MqAction.CommitMessage;
        }

        CreateFlowWashReportRequest request =  JSON.parseObject(msg, CreateFlowWashReportRequest.class);

        if (request == null) {
            log.info("changeRequest 为空!");
            return MqAction.CommitMessage;
        }

        if (request.getYear() == null || request.getMonth() == null || request.getYear() == 0 || request.getMonth()  > 12) {

            log.error("changeRequest 异常!,{}",request);
            return MqAction.CommitMessage;
        }

        Map<CreateFlowWashReportRequest.ReportTypeEnum, Consumer<CreateFlowWashReportRequest>> reportCreateHandlerMap  = Maps.newHashMapWithExpectedSize(3);
        reportCreateHandlerMap.put(CreateFlowWashReportRequest.ReportTypeEnum.SUPPLY,(t) -> flowWashSupplyStockReportHandler.generator(t));
        reportCreateHandlerMap.put(CreateFlowWashReportRequest.ReportTypeEnum.HOSPITAL,(t) -> flowWashHospitalStockReportHandler.generator(t));
        reportCreateHandlerMap.put(CreateFlowWashReportRequest.ReportTypeEnum.PHARMACY,(t) -> flowWashPharmacyPurchaseReportHandler.generator(t));

        reportCreateHandlerMap.get(request.getReportType()).accept(request);

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
