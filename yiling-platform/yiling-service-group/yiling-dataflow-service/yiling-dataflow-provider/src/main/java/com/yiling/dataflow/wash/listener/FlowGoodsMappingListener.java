package com.yiling.dataflow.wash.listener;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.service.FlowEnterpriseGoodsMappingService;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/3/9
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GOODS_MAPPING_MONTH_WASH, consumerGroup = Constants.TAG_GOODS_MAPPING_MONTH_WASH,maxThread = 3)
public class FlowGoodsMappingListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired(required = false)
    private RocketMqProducerService           rocketMqProducerService;
    @Autowired
    private FlowEnterpriseGoodsMappingService flowEnterpriseGoodsMappingService;
    @Autowired
    private FlowMonthWashControlService       flowMonthWashControlService;
    @Autowired
    private FlowSaleWashService               flowSaleWashService;
    @Autowired
    private FlowPurchaseWashService           flowPurchaseWashService;
    @Autowired
    private FlowGoodsBatchDetailWashService   flowGoodsBatchDetailWashService;
    @Autowired
    private CrmGoodsInfoService               crmGoodsInfoService;
    @Autowired
    private FlowMonthWashTaskService          flowMonthWashTaskService;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        List<FlowEnterpriseGoodsMappingDTO> flowEnterpriseGoodsMappingDTOList = JSON.parseArray(msg, FlowEnterpriseGoodsMappingDTO.class);
        //更新月流向映射数据
        flowMonthWashTaskService.updateMonthFlowByGoodsMapping(flowEnterpriseGoodsMappingDTOList);
        //更新日流向
        flowMonthWashTaskService.updateDayFlowByGoodsMapping(flowEnterpriseGoodsMappingDTOList);
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
