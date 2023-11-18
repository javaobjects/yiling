package com.yiling.export.imports.consumer;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadTypeEnum;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.export.excel.service.ExcelTaskConfigService;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.flow.api.FixMonthFlowApi;
import com.yiling.sjms.flow.dto.request.FixMonthFlowImportMq;
import com.yiling.sjms.flow.dto.request.SaveSubFormRequest;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 补传月流向审批通过后消费监听
 * @author: gxl
 * @date: 2023/6/28
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_IMPORT_FIX_MONTH_FLOW, consumerGroup = Constants.TOPIC_IMPORT_FIX_MONTH_FLOW)
public class FixMonthFlowListener extends AbstractMessageListener {
    @Autowired
    ExcelTaskConfigService excelTaskConfigService;
    @Autowired
    ExcelTaskRecordService excelTaskRecordService;
    @DubboReference
    FlowMonthUploadRecordApi flowMonthUploadRecordApi;

    @DubboReference
    FixMonthFlowApi fixMonthFlowApi;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("FixMonthFlowListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        FixMonthFlowImportMq fixMonthFlowImportMq = JSON.parseObject(msg, FixMonthFlowImportMq.class);
        this.createImportTask(fixMonthFlowImportMq.getFileName(),fixMonthFlowImportMq.getFileKey(),fixMonthFlowImportMq.getOpUserId(),fixMonthFlowImportMq.getMonthFlowFormId());
        return   MqAction.CommitMessage;
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
    private void createImportTask(String fileName,String fileKey,Long opUserId,Long monthFlowFormId){
        // 根据文件名获取excelCode
        String excelCode = flowMonthUploadRecordApi.getExcelCodeByFileName(fileName);
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigService.findExcelTaskConfigByCode(excelCode);

        SaveExcelTaskRecordRequest recordRequest = new SaveExcelTaskRecordRequest();
        recordRequest.setClassName(excelTaskConfigDTO.getClassName());
        recordRequest.setVerifyHandlerBeanName(excelTaskConfigDTO.getVerifyHandlerBeanName());
        recordRequest.setImportDataHandlerBeanName(excelTaskConfigDTO.getImportDataHandlerBeanName());
        recordRequest.setModelClass(excelTaskConfigDTO.getModelClass());
        recordRequest.setEid(0L);
        recordRequest.setOpUserId(opUserId);
        recordRequest.setTitle(excelTaskConfigDTO.getTitle());
        recordRequest.setMenuName(excelTaskConfigDTO.getMenuName());
        recordRequest.setTaskConfigId(excelTaskConfigDTO.getId());
        recordRequest.setSource(ExcelSourceEnum.DIH.getCode());
        recordRequest.setFileName(fileName);
        recordRequest.setRequestUrl(fileKey);
        try {
            Long recordId = excelTaskRecordService.saveExcelTaskRecord(recordRequest);
            SaveFlowMonthUploadRecordRequest request = new SaveFlowMonthUploadRecordRequest();
            request.setRecordId(recordId);
            request.setFileName(fileName);
            request.setFileUrl(fileKey);
            request.setImportStatus(FlowMonthUploadImportStatusEnum.IN_PROGRESS.getCode());
            request.setDataType(flowMonthUploadRecordApi.getDataType(fileName));
            request.setOpUserId(opUserId);
            request.setRemark("补传");
            request.setUploadType(FlowMonthUploadTypeEnum.FIX.getCode());
            Long id = flowMonthUploadRecordApi.saveFlowMonthRecord(request);
            SaveSubFormRequest updateFlowMonthRecord = new  SaveSubFormRequest();
            updateFlowMonthRecord.setId(monthFlowFormId).setFlowMonthUploadId(id);
            fixMonthFlowApi.updateFlowMonthRecord(updateFlowMonthRecord);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
        }
    }
}