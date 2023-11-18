package com.yiling.sjms.gb.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractMateFlowRequest;
import com.yiling.dataflow.gb.enums.GbExecTypeEnum;
import com.yiling.dataflow.gb.enums.GbLockTypeEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购流向数据扣减
 *
 * @author: shuang.zhang
 * @date: 2023/5/11
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_SALE_GB_SUBSTRACT_TASK, consumerGroup = Constants.TAG_FLOW_SALE_GB_SUBSTRACT_TASK, maxThread = 1)
public class GbFlowSaleSubstractListener extends AbstractMessageListener {

    @DubboReference
    private GbAppealFormApi gbAppealFormApi;
    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        if (StringUtils.isBlank(msg)) {
            log.error("消息为空, MsgId:{}, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            return MqAction.CommitMessage;
        }
        //申请表ID
        Long id = Long.parseLong(msg);
        //申请类型
        String tag = message.getTags();
        // type=1 锁定  type=2非锁定
        Integer type = Convert.toInt(tag);
        String typeName = "";
        // 通过不同类型查询日程
        FlowMonthWashControlDTO flowMonthWashControlDTO;
        if (GbLockTypeEnum.LOCK.getCode().equals(type)) {
            typeName = "团购锁定";
            flowMonthWashControlDTO = flowMonthWashControlApi.getGbLockStatus();
        } else {
            typeName = "团购非锁定";
            flowMonthWashControlDTO = flowMonthWashControlApi.getGbUnlockStatus();
        }
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            String operType = "";
            GbLockTypeEnum fromCode = GbLockTypeEnum.getFromCode(type);
            if (ObjectUtil.isNotNull(fromCode)) {
                operType = fromCode.getMessage();
            }
            log.warn(typeName + "日程还没有开启, 不能自动扣减" + operType +", MsgId:{}, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            return MqAction.CommitMessage;
        }

        // 源流向数量扣减
        GbAppealSubstractMateFlowRequest request = new GbAppealSubstractMateFlowRequest();
        request.setAppealFormId(id);
        request.setExecType(GbExecTypeEnum.AUTO.getCode());
        request.setOpUserId(0L);
        gbAppealFormApi.substractMateFlow(request);
        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 0;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return null;
    }
}
