package com.yiling.sjms.gb.listener;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.order.util.Constants;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.gb.handler.ReportRelationShipCompleteHandler;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.framework.common.util.Constants.TAG_UPDATE_SALE_REPORT_CRM_RELATIONSHIP;
import static com.yiling.framework.common.util.Constants.TOPIC_UPDATE_SALE_REPORT_CRM_RELATIONSHIP;


/**
 * 更新销售报表中三者关系数据
 */
@Slf4j
@RocketMqListener(topic = TOPIC_UPDATE_SALE_REPORT_CRM_RELATIONSHIP, consumerGroup = TAG_UPDATE_SALE_REPORT_CRM_RELATIONSHIP, maxThread = 3)
public class ReportRelationShipCompleteListener extends AbstractMessageListener {
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private ReportRelationShipCompleteHandler reportRelationShipCompleteHandler;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info(MessageFormat.format(Constants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));

        if (StringUtils.isBlank(msg)) {

            log.warn("消息为空");
            return MqAction.CommitMessage;
        }

        List<String> flowWashIdList = StrUtil.split(msg,',',true,true);

        boolean result = reportRelationShipCompleteHandler.completeSaleReportRelationShip(flowWashIdList);

        if (result) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;

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
