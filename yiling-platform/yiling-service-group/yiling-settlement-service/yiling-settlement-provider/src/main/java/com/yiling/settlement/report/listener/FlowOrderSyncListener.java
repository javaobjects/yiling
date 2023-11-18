package com.yiling.settlement.report.listener;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.dataflow.relation.dto.FlowGoodsRelationEditTaskDTO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.settlement.report.enums.ReportSyncTaskEnum;
import com.yiling.settlement.report.service.FlowSaleOrderSyncService;
import com.yiling.settlement.report.service.ReportSyncTaskService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_GOODS_RELATION_EDIT_TASK_SEND, consumerGroup = Constants.TOPIC_FLOW_GOODS_RELATION_EDIT_TASK_SEND)
public class FlowOrderSyncListener implements MessageListener {

    @Autowired
    FlowSaleOrderSyncService flowSaleOrderSyncService;
    @Autowired
    ReportSyncTaskService reportSyncTaskService;

    @DubboReference
    FlowGoodsRelationEditTaskApi flowGoodsRelationEditTaskApi;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            if (StrUtil.isBlank(msg)) {
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }
            Type type = new TypeToken<List<Long>>() {
            }.getType();
            List<Long> taskIdList = JSONObject.parseObject(msg).getObject("taskIds",type);
            List<FlowGoodsRelationEditTaskDTO> taskList = flowGoodsRelationEditTaskApi.getByIdList(taskIdList);
            taskList.stream().forEach(task->{
                long[] list = StrUtil.splitToLong(task.getFlowSaleIds(), ",");
                //同步订单
                Boolean isCreate = flowSaleOrderSyncService.syncFlowOrder(Arrays.stream(list).boxed().collect(Collectors.toList()));
                //同步失败存入报表任务同步表，定时执行
                if (!isCreate) {
                    reportSyncTaskService.addSyncTask(task.getId().toString(), ReportSyncTaskEnum.FLOW_ORDER);
                    return;
                }
                //更新任务状态
                Integer row = flowGoodsRelationEditTaskApi.updateSyncStatusByIdList(ListUtil.toList(task.getId()), FlowGoodsRelationEditTaskSyncStatusEnum.DONE.getCode());
                if (row<=0){
                    log.error("同步流向订单成功后，更新同步任务状态失败，参数={}",ListUtil.toList(task.getId()));
                }
            });
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
