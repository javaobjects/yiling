package com.yiling.export.export.consumer;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.export.export.service.ExportTaskRecordService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 */
@Slf4j
@RocketMqListener(topic = "export_file_center", consumerGroup = "export_file_center")
public class ExportMqListener extends AbstractMessageListener {

    @Autowired
    private ExportTaskRecordService exportTaskRecordService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        exportTaskRecordService.syncUploadExportTask(Long.parseLong(msg));
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
