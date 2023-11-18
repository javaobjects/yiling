package com.yiling.dataflow.report.listener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.dataflow.gb.service.GbAppealAllocationService;
import com.yiling.dataflow.report.handler.FlowWashSaleReportHandler;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购处理流向生成
 *
 * @author zhigang.guo
 * @date: 2023/5/16
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GB_FLOW_NOTIFY, consumerGroup = Constants.TAG_GB_FLOW_NOTIFY,maxThread = 3)
public class GbFlowFinishListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    private GbAppealAllocationService gbAppealAllocationService;
    @Autowired
    private FlowWashSaleReportHandler flowWashSaleReportHandler;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (msg == null) {

            log.info("msg 为空!");
            return MqAction.CommitMessage;
        }

        List<Long> idList = Arrays.stream(StrUtil.split(msg, ",")).map(t -> Long.valueOf(t)).collect(Collectors.toList());
        List<GbAppealAllocationDO> gbAppealAllocationDOList = gbAppealAllocationService.listByIds(idList);

        if (CollectionUtil.isEmpty(gbAppealAllocationDOList)) {

            log.warn("未查询到团购处理结果数据");
            return MqAction.CommitMessage;
        }

        if (flowWashSaleReportHandler.batchSaveOrUpdateGbFlowSale(gbAppealAllocationDOList)) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;
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
