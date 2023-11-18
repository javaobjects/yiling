package com.yiling.dataflow.report.listener;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.report.dto.request.FlowWashChangeRequest;
import com.yiling.dataflow.report.handler.FlowWashInventoryReportHandler;
import com.yiling.dataflow.report.handler.FlowWashSaleReportHandler;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/** 清洗任务变更通知
 * @author zhigang.guo
 * @date: 2023/3/3
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_WASH_TASK_CHANGE_NOTIFY, consumerGroup = Constants.TAG_WASH_TASK_CHANGE_NOTIFY,maxThread = 3)
public class FlowMonthWashTaskChangeListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    private FlowSaleWashService flowSaleWashService;
    @Autowired
    private FlowGoodsBatchDetailWashService flowGoodsBatchDetailWashService;
    @Autowired
    private FlowWashSaleReportHandler flowWashSaleReportHandler;
    @Autowired
    private FlowWashInventoryReportHandler inventoryReportHandler;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg  = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (msg == null) {
            log.info("msg 为空!");
            return MqAction.CommitMessage;
        }

        FlowWashChangeRequest changeRequest =  JSON.parseObject(body, FlowWashChangeRequest.class);

        if (changeRequest.getYear() == null || changeRequest.getMonth() == null || changeRequest.getYear() == 0 || changeRequest.getMonth()  > 12 ) {

            log.error("changeRequest 异常!,{}",changeRequest);
            return MqAction.CommitMessage;
        }


        Map<FlowWashChangeRequest.ChangeTypeEnum, Function<FlowWashChangeRequest,MqAction>> functionMap = Maps.newHashMapWithExpectedSize(2);

        // 销售流向报表
        functionMap.put(FlowWashChangeRequest.ChangeTypeEnum.SALE,(t) -> {
            List<FlowSaleWashDO> flowSaleWashDOs = flowSaleWashService.listByIds(t.getFlowWashIds());

            if (CollectionUtil.isEmpty(flowSaleWashDOs)) {

                return MqAction.CommitMessage;
            }

            flowSaleWashDOs = flowSaleWashDOs.stream()
                     // 正常，疑似重复
                    .filter(z -> ObjectUtil.equal(FlowDataWashStatusEnum.NORMAL.getCode(),z.getWashStatus()) || ObjectUtil.equal(FlowDataWashStatusEnum.DUPLICATE.getCode(),z.getWashStatus()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isEmpty(flowSaleWashDOs)) {

                log.warn("清洗数据已删除");
                return MqAction.CommitMessage;
            }

            flowWashSaleReportHandler.generator(flowSaleWashDOs,t.getYear(),t.getMonth());

            return MqAction.CommitMessage;
        });

        // 销售库存表变更
        functionMap.put(FlowWashChangeRequest.ChangeTypeEnum.INVENTORY,(t) -> {

            List<FlowGoodsBatchDetailWashDO> goodsBatchTaskDOs = flowGoodsBatchDetailWashService.listByIds(t.getFlowWashIds());

            if (CollectionUtil.isEmpty(goodsBatchTaskDOs)) {

                log.info("goodsBatchTaskDOs 为空!");
                return MqAction.CommitMessage;
            }

            goodsBatchTaskDOs = goodsBatchTaskDOs.stream()
                    // 正常，疑似重复
                    .filter(z -> ObjectUtil.equal(FlowDataWashStatusEnum.NORMAL.getCode(),z.getWashStatus()) || ObjectUtil.equal(FlowDataWashStatusEnum.DUPLICATE.getCode(),z.getWashStatus()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isEmpty(goodsBatchTaskDOs)) {

                log.warn("清洗数据已删除");
                return MqAction.CommitMessage;
            }

            inventoryReportHandler.generator(goodsBatchTaskDOs,t.getYear(),t.getMonth());

            return MqAction.CommitMessage;
        });

        return functionMap.get(changeRequest.getChangeTypeEnum()).apply(changeRequest);
    }

    @Override
    protected int getMaxReconsumeTimes() {

        return 5;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}
