package com.yiling.dataflow.wash.listener;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
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
@RocketMqListener(topic = Constants.TOPIC_CUSTOMER_MAPPING_MONTH_WASH, consumerGroup = Constants.TAG_CUSTOMER_MAPPING_MONTH_WASH,maxThread = 3)
public class FlowCustomerMappingListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired(required = false)
    private RocketMqProducerService              rocketMqProducerService;
    @Autowired
    private FlowEnterpriseCustomerMappingService flowEnterpriseCustomerMappingService;
    @Autowired
    private FlowMonthWashControlService          flowMonthWashControlService;
    @Autowired
    private CrmEnterpriseService                 crmEnterpriseService;
    @Autowired
    private FlowSaleWashService                  flowSaleWashService;
    @Autowired
    private FlowMonthWashTaskService             flowMonthWashTaskService;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        List<FlowEnterpriseCustomerMappingDTO> flowEnterpriseCustomerMappingDTOList = JSON.parseArray(msg, FlowEnterpriseCustomerMappingDTO.class);
        flowMonthWashTaskService.updateMonthFlowByCustomerMapping(flowEnterpriseCustomerMappingDTOList);
        flowMonthWashTaskService.updateDayFlowByCustomerMapping(flowEnterpriseCustomerMappingDTOList);
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
