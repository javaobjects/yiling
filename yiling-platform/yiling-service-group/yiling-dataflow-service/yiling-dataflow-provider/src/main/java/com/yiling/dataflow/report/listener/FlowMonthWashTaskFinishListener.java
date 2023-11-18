package com.yiling.dataflow.report.listener;

import java.util.Map;
import java.util.function.Function;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.report.handler.FlowWashInventoryReportHandler;
import com.yiling.dataflow.report.handler.FlowWashSaleReportHandler;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/** 清洗任务完成通知消息
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@RefreshScope
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_WASH_TASK_FINISH_NOTIFY, consumerGroup = Constants.TAG_WASH_TASK_FINISH_NOTIFY,maxThread = 5)
public class FlowMonthWashTaskFinishListener extends AbstractMessageListener {

    /**每次生成报表长度*/
    @Value("${wash.page.size:2000}")
    private Integer                          pageSize;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    private FlowWashSaleReportHandler flowWashSaleReportHandler;
    @Autowired
    private FlowWashInventoryReportHandler inventoryReportHandler;
    @Autowired
    private FlowMonthWashTaskService flowMonthWashTaskService;
    @Autowired
    private FlowSaleWashService flowSaleWashService;
    @Autowired
    private FlowGoodsBatchDetailWashService flowGoodsBatchDetailWashService;


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (msg == null) {
            log.info("msg 为空!");
            return MqAction.CommitMessage;
        }

        Long taskId = Long.valueOf(msg);

        FlowMonthWashTaskDO washTaskDO = flowMonthWashTaskService.getById(taskId);

        if (washTaskDO == null) {
            log.info("washTaskDO 为空!");
            return MqAction.CommitMessage;
        }

        if (Constants.IS_YES == washTaskDO.getReportConsumeStatus()) {

            log.warn("清洗任务完成表消息重复");
            return MqAction.CommitMessage;
        }

        Map<FlowTypeEnum, Function<FlowMonthWashTaskDO,Boolean>> functionMap = Maps.newHashMapWithExpectedSize(3);

        // 销量清洗任务
        functionMap.put(FlowTypeEnum.SALE,this::createFlowWashSaleReport);
        // 库存清洗任务
        functionMap.put(FlowTypeEnum.GOODS_BATCH,this::createFlowWashInventoryReport);
        // 采购
        functionMap.put(FlowTypeEnum.PURCHASE,(t) -> true);

        Boolean effectResult = functionMap.get(FlowTypeEnum.getByCode(washTaskDO.getFlowType())).apply(washTaskDO);


        if (!effectResult) {

            return MqAction.ReconsumeLater;
        }

        return MqAction.CommitMessage;
    }


    /**
     * 销量清洗任务
     * @param washTaskDO
     * @return
     */
    private boolean createFlowWashSaleReport(FlowMonthWashTaskDO washTaskDO) {

       Page<FlowSaleWashDO> page;
        int current = 1;
        int size = pageSize;

        QueryFlowSaleWashPageRequest request = new QueryFlowSaleWashPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setFmwtId(washTaskDO.getId());
            request.setWashStatusList(ListUtil.toList(FlowDataWashStatusEnum.NORMAL.getCode(),FlowDataWashStatusEnum.DUPLICATE.getCode()));

            page = flowSaleWashService.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            // 执行报表生成
            flowWashSaleReportHandler.generator(page.getRecords(),washTaskDO.getYear(),washTaskDO.getMonth());

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        return this.updateWashTaskReportConsumeStatus(washTaskDO.getId(),Constants.IS_YES);
    }

    /**
     * 生成库存清洗报表任务
     * @param washTaskDO
     * @return
     */
    private boolean createFlowWashInventoryReport(FlowMonthWashTaskDO washTaskDO) {

        Page<FlowGoodsBatchDetailWashDO> page;
        int current = 1;
        int size = pageSize;

        QueryFlowGoodsBatchDetailWashPageRequest request = new QueryFlowGoodsBatchDetailWashPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setFmwtId(washTaskDO.getId());
            request.setWashStatusList(ListUtil.toList(FlowDataWashStatusEnum.NORMAL.getCode(),FlowDataWashStatusEnum.DUPLICATE.getCode()));

            page = flowGoodsBatchDetailWashService.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            // 执行报表生成
            inventoryReportHandler.generator(page.getRecords(),washTaskDO.getYear(),washTaskDO.getMonth());

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        return this.updateWashTaskReportConsumeStatus(washTaskDO.getId(),Constants.IS_YES);

    }


    /**
     * 修改清洗任务状态
     * @param id
     * @param status
     * @return
     */
    private boolean updateWashTaskReportConsumeStatus(Long id,Integer status) {

        FlowMonthWashTaskDO updateTask = new FlowMonthWashTaskDO();
        updateTask.setId(id);
        updateTask.setReportConsumeStatus(status);

        return flowMonthWashTaskService.updateById(updateTask);
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
