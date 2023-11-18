package com.yiling.dataflow.wash.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowMonthWashTaskStatusRequest;
import com.yiling.dataflow.wash.dto.request.UpdateReportConsumeStatusRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlStatusEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.enums.FlowWashTaskStatusEnum;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Slf4j
@DubboService
public class FlowMonthWashTaskApiImpl implements FlowMonthWashTaskApi {

    @Autowired
    private FlowMonthWashTaskService flowMonthWashTaskService;

    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;

    @Autowired
    private FlowSaleWashService flowSaleWashService;

    @Autowired
    private FlowPurchaseWashService flowPurchaseWashService;

    @Autowired
    private FlowGoodsBatchDetailWashService flowGoodsBatchDetailWashService;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public Page<FlowMonthWashTaskDTO> listPage(QueryFlowMonthWashTaskPageRequest request) {
        return PojoUtils.map(flowMonthWashTaskService.listPage(request), FlowMonthWashTaskDTO.class);
    }

    @Override
    public long create(SaveFlowMonthWashTaskRequest request, boolean isSendMq) {
        return flowMonthWashTaskService.create(request,isSendMq);
    }

    @Override
    public List<Long> getCrmEnterIdsByFmwcIdAndClassify(long fmwcId, int flowClassify) {
        return flowMonthWashTaskService.getCrmEnterIdsByFmwcIdAndClassify(fmwcId,flowClassify);
    }


    @Override
    public void confirm(Long id) {
        flowMonthWashTaskService.confirm(id);
    }

    @Override
    public void deleteTaskAndFlowDataById(Long id) {
        flowMonthWashTaskService.deleteTaskAndFlowDataById(id);
    }

    @Override
    public FlowMonthWashTaskDTO getById(Long id) {
        return PojoUtils.map(flowMonthWashTaskService.getById(id), FlowMonthWashTaskDTO.class);
    }

    @Override
    public int updateReportConsumeStatusById(UpdateReportConsumeStatusRequest request) {
        return flowMonthWashTaskService.updateReportConsumeStatusById(request.getId(), request.getReportConsumeStatus());
    }

    @Override
    public void reWash(Long fmwcId) {
        //  判断当前日程状态
        FlowMonthWashControlDO flowMonthWashControlDO = flowMonthWashControlService.getById(fmwcId);
        if (flowMonthWashControlDO.getWashStatus()!=1) {
            // 已结束不可重新清洗
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_STATUS_FINISH);
        }

        //  获取当前日程下所有月流向任务
        List<FlowMonthWashTaskDO> flowMonthWashTaskDOList = flowMonthWashTaskService.getByControlId(fmwcId);

        //  判断任务如果有未清洗以及清洗中的，则不重新清洗
        for (FlowMonthWashTaskDO flowMonthWashTaskDO : flowMonthWashTaskDOList) {
            if (FlowWashTaskStatusEnum.NOT_WASH.getCode().equals(flowMonthWashTaskDO.getWashStatus())
                    || FlowWashTaskStatusEnum.WASHING.getCode().equals(flowMonthWashTaskDO.getWashStatus())) {
                throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_STATUS_FINISH);
            }
        }

        //  修改所有月流向任务清洗状态，并删除对应任务的清洗数据
        for (FlowMonthWashTaskDO flowMonthWashTaskDO : flowMonthWashTaskDOList) {
            //  状态改为未清洗
            flowMonthWashTaskDO.setWashStatus(FlowWashTaskStatusEnum.NOT_WASH.getCode());

            //  删除旧数据
            if (FlowTypeEnum.PURCHASE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
                LambdaUpdateWrapper<FlowPurchaseWashDO> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(FlowPurchaseWashDO::getFmwtId, flowMonthWashTaskDO.getId());

                FlowPurchaseWashDO entity = new FlowPurchaseWashDO();
                entity.setOpTime(new Date());
                flowPurchaseWashService.batchDeleteWithFill(entity, wrapper);
            } else if (FlowTypeEnum.SALE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
                LambdaUpdateWrapper<FlowSaleWashDO> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(FlowSaleWashDO::getFmwtId, flowMonthWashTaskDO.getId());

                FlowSaleWashDO entity = new FlowSaleWashDO();
                entity.setOpTime(new Date());
                flowSaleWashService.batchDeleteWithFill(entity, wrapper);
            } else if (FlowTypeEnum.GOODS_BATCH.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
                LambdaUpdateWrapper<FlowGoodsBatchDetailWashDO> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(FlowGoodsBatchDetailWashDO::getFmwtId, flowMonthWashTaskDO.getId());

                FlowGoodsBatchDetailWashDO entity = new FlowGoodsBatchDetailWashDO();
                entity.setOpTime(new Date());
                flowGoodsBatchDetailWashService.batchDeleteWithFill(entity, wrapper);
            }

            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, DateUtil.formatDate(new Date()), String.valueOf(flowMonthWashTaskDO.getId()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("月流向重新清洗，清洗任务id={}, 消息发送失败！", flowMonthWashTaskDO.getId());
            }
        }

    }

    @Override
    public int updateWashStatusById(Long id, Integer washStatus) {
        return flowMonthWashTaskService.updateWashStatusById(id, washStatus);
    }

    @Override
    public List<FlowMonthWashTaskDTO> getByControlId(Long fmwcId) {
        return PojoUtils.map(flowMonthWashTaskService.getByControlId(fmwcId),FlowMonthWashTaskDTO.class);
    }


}
