package com.yiling.settlement.report.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.dataflow.relation.dto.FlowGoodsRelationEditTaskDTO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.settlement.report.dto.request.CreateReportFlowRequest;
import com.yiling.settlement.report.entity.ReportTaskDO;
import com.yiling.settlement.report.enums.ReportTaskStatusEnum;
import com.yiling.settlement.report.enums.ReportTypeEnum;
import com.yiling.settlement.report.service.ReportService;
import com.yiling.settlement.report.service.ReportTaskService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_REPORT_REWARD_FLOW_SEND, consumerGroup = Constants.TAG_REPORT_REWARD_FLOW_SEND)
public class FlowReportRewardListener extends AbstractMessageListener {

    @Autowired
    ReportService reportService;
    @Autowired
    ReportTaskService reportTaskService;

    @DubboReference
    FlowGoodsRelationEditTaskApi flowGoodsRelationEditTaskApi;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    //    @MdcLog
    //    @Override
    //    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
    //        String msg = null;
    //        Long reportTaskId = null;
    //        try {
    //            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
    //            if (StrUtil.isBlank(msg)) {
    //                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //                return MqAction.CommitMessage;
    //            }
    //            CreateReportFlowRequest request = JSONObject.parseObject(msg, CreateReportFlowRequest.class);
    //            //校验是否有在修改以岭品关系的任务
    //            List<FlowGoodsRelationEditTaskDTO> reviseTaskList = flowGoodsRelationEditTaskApi.getListByEid(request.getEid()).stream().filter(e->ObjectUtil.equal(e.getSyncStatus(), FlowGoodsRelationEditTaskSyncStatusEnum.WAIT.getCode())).collect(Collectors.toList());
    //            if (CollUtil.isNotEmpty(reviseTaskList)){
    //                ReportTaskDO reportTaskDO=new ReportTaskDO();
    //                //插入报表任务
    //                reportTaskDO.setEid(request.getEid());
    //                reportTaskDO.setParameterJson(msg);
    //                reportTaskDO.setOpTime(new Date());
    //                reportTaskDO.setType(ReportTypeEnum.FLOW.getCode());
    //                reportTaskDO.setTaskNumber(UUID.randomUUID().toString());
    //                reportTaskDO.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
    //                reportTaskDO.setErrMsg("该企业存在正在修改以岭品关系的任务，任务="+ JSONObject.toJSONString(reviseTaskList));
    //                reportTaskService.save(reportTaskDO);
    //                log.warn("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{该企业存在正在修改以岭品关系的任务，任务="+ JSONObject.toJSONString(reviseTaskList)+"}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //                return MqAction.CommitMessage;
    //            }
    //            List<ReportTaskDO> list = reportTaskService.queryInProductionTask(request.getEid());
    //            if (CollUtil.isNotEmpty(list)){
    //                ReportTaskDO reportTaskDO=new ReportTaskDO();
    //                //插入报表任务
    //                reportTaskDO.setEid(request.getEid());
    //                reportTaskDO.setParameterJson(msg);
    //                reportTaskDO.setOpTime(new Date());
    //                reportTaskDO.setType(ReportTypeEnum.FLOW.getCode());
    //                reportTaskDO.setTaskNumber(UUID.randomUUID().toString());
    //                reportTaskDO.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
    //                reportTaskDO.setErrMsg("该企业存在正在生成的报表，任务="+ JSONObject.toJSONString(list));
    //                reportTaskService.save(reportTaskDO);
    //                log.warn("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{该企业存在正在生成的报表，任务="+ JSONObject.toJSONString(list)+"}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //                return MqAction.CommitMessage;
    //            }
    //            //插入报表任务
    //            ReportTaskDO reportTaskDO=new ReportTaskDO();
    //            reportTaskDO.setEid(request.getEid());
    //            reportTaskDO.setParameterJson(msg);
    //            reportTaskDO.setOpTime(new Date());
    //            reportTaskDO.setType(ReportTypeEnum.FLOW.getCode());
    //            reportTaskDO.setTaskNumber(UUID.randomUUID().toString());
    //            boolean isSave = reportTaskService.save(reportTaskDO);
    //            if (!isSave){
    //                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{插入报表生成任务表异常}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //                return MqAction.ReconsumeLater;
    //            }
    //            reportTaskId=reportTaskDO.getId();
    //
    //            //生成报表
    //            Long reportId = reportService.createFlowReport(request);
    //            //更新任务状态
    //            ReportTaskDO updatePar=new ReportTaskDO();
    //            updatePar.setId(reportTaskId);
    //            updatePar.setOpTime(new Date());
    //            if (ObjectUtil.isNull(reportId)) {
    //                updatePar.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
    //                updatePar.setErrMsg("返回的报表id为null");
    //                boolean isUpdate =reportTaskService.updateById(updatePar);
    //                if (!isUpdate){
    //                    log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{报表生成失败，返回的报表id为0且更新任务状态失败}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //                }
    //                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //                return MqAction.ReconsumeLater;
    //            }
    //            updatePar.setReportId(reportId);
    //            updatePar.setStatus(ReportTaskStatusEnum.TASK_SUCCESS.getCode());
    //            boolean isUpdate = reportTaskService.updateById(updatePar);
    //            if (!isUpdate){
    //                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{报表生成成功，但更新报表任务状态异常，reportId="+reportId+"}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //            }
    //
    //            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //        } catch (Exception e) {
    //            ReportTaskDO updatePar=new ReportTaskDO();
    //            updatePar.setId(reportTaskId);
    //            updatePar.setOpTime(new Date());
    //            updatePar.setErrMsg(e.getMessage());
    //            updatePar.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
    //            boolean isUpdate = reportTaskService.updateById(updatePar);
    //            if (!isUpdate){
    //                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{报表生成失败，更新报表任务状态失败}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
    //            }
    //            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
    //            return MqAction.ReconsumeLater;
    //        }
    //        return MqAction.CommitMessage;
    //    }

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        CreateReportFlowRequest request = JSONObject.parseObject(body, CreateReportFlowRequest.class);
        //校验是否有在修改以岭品关系的任务
        List<FlowGoodsRelationEditTaskDTO> reviseTaskList = flowGoodsRelationEditTaskApi.getListByEid(request.getEid()).stream().filter(e -> ObjectUtil.equal(e.getSyncStatus(), FlowGoodsRelationEditTaskSyncStatusEnum.WAIT.getCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(reviseTaskList)) {
            ReportTaskDO reportTaskDO = new ReportTaskDO();
            //插入报表任务
            reportTaskDO.setEid(request.getEid());
            reportTaskDO.setParameterJson(body);
            reportTaskDO.setOpTime(new Date());
            reportTaskDO.setType(ReportTypeEnum.FLOW.getCode());
            reportTaskDO.setTaskNumber(UUID.randomUUID().toString());
            reportTaskDO.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
            reportTaskDO.setErrMsg("该企业存在正在修改以岭品关系的任务，任务=" + JSONObject.toJSONString(reviseTaskList));
            reportTaskService.save(reportTaskDO);
            log.warn("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{该企业存在正在修改以岭品关系的任务，任务=" + JSONObject.toJSONString(reviseTaskList) + "}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            return MqAction.CommitMessage;
        }
        List<ReportTaskDO> list = reportTaskService.queryInProductionTask(request.getEid());
        if (CollUtil.isNotEmpty(list)) {
            ReportTaskDO reportTaskDO = new ReportTaskDO();
            //插入报表任务
            reportTaskDO.setEid(request.getEid());
            reportTaskDO.setParameterJson(body);
            reportTaskDO.setOpTime(new Date());
            reportTaskDO.setType(ReportTypeEnum.FLOW.getCode());
            reportTaskDO.setTaskNumber(UUID.randomUUID().toString());
            reportTaskDO.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
            reportTaskDO.setErrMsg("该企业存在正在生成的报表，任务=" + JSONObject.toJSONString(list));
            reportTaskService.save(reportTaskDO);
            log.warn("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{该企业存在正在生成的报表，任务=" + JSONObject.toJSONString(list) + "}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            return MqAction.CommitMessage;
        }
        //插入报表任务
        ReportTaskDO reportTaskDO = new ReportTaskDO();
        reportTaskDO.setEid(request.getEid());
        reportTaskDO.setParameterJson(body);
        reportTaskDO.setOpTime(new Date());
        reportTaskDO.setType(ReportTypeEnum.FLOW.getCode());
        reportTaskDO.setTaskNumber(UUID.randomUUID().toString());
        boolean isSave = reportTaskService.save(reportTaskDO);
        if (!isSave) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{插入报表生成任务表异常}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            throw new ServiceException(ResultCode.FAILED);
        }
        Long reportTaskId = reportTaskDO.getId();

        //生成报表
        Long reportId = reportService.createFlowReport(request);
        //更新任务状态
        ReportTaskDO updatePar = new ReportTaskDO();
        updatePar.setId(reportTaskId);
        updatePar.setOpTime(new Date());
        if (ObjectUtil.isNull(reportId)) {
            updatePar.setStatus(ReportTaskStatusEnum.TASK_FAIL.getCode());
            updatePar.setErrMsg("返回的报表id为null");
            boolean isUpdate = reportTaskService.updateById(updatePar);
            if (!isUpdate) {
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{报表生成失败，返回的报表id为0且更新任务状态失败}", message.getMsgId(), message.getTopic(), message.getTags(), body);
                throw new ServiceException(ResultCode.FAILED);
            }
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            throw new ServiceException(ResultCode.FAILED);
        }
        updatePar.setReportId(reportId);
        updatePar.setStatus(ReportTaskStatusEnum.TASK_SUCCESS.getCode());
        boolean isUpdate = reportTaskService.updateById(updatePar);
        if (!isUpdate) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{报表生成成功，但更新报表任务状态异常，reportId=" + reportId + "}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            throw new ServiceException(ResultCode.FAILED);
        }

        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
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
