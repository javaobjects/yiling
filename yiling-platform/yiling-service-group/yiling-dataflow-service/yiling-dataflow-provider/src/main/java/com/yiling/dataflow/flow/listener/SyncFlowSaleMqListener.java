package com.yiling.dataflow.flow.listener;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.flow.FlowUtils;
import com.yiling.dataflow.flow.dao.SyncFlowSaleMapper;
import com.yiling.dataflow.flow.handler.FlowGoodsRelationTaskHandler;
import com.yiling.dataflow.flow.service.FlowSaleSummaryService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 *
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_FLOW_SALE_SUMMARY_SYNC, consumerGroup = Constants.TOPIC_FLOW_SALE_SUMMARY_SYNC)
public class SyncFlowSaleMqListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired
    private FlowSaleSummaryService flowSaleSummaryService;
    @Autowired
    private FlowGoodsRelationTaskHandler flowGoodsRelationTaskHandler;
    @Autowired
    private SyncFlowSaleMapper syncFlowSaleMapper;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.debug("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            List<Long> ids = JSONArray.parseArray(msg, Long.class);
//            for (Long id : ids) {
//                flowSaleSummaryService.updateFlowSaleSummaryByFlowSaleId(id);
//            }
            // 销售匹配yiGoodsId变更时，保存流向销售单主键id的修改任务，并发送消息给流向报表
            flowGoodsRelationTaskHandler.flowSaleYlGoodsIdSendMsg(ids);
            // 删除已消费处理的id
            deleteSyncFlowSale(ids);
            return MqAction.CommitMessage;
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw new RuntimeException(e);
        }
    }

    private void deleteSyncFlowSale(List<Long> flowSaleIdList) {
        if (CollUtil.isEmpty(flowSaleIdList)) {
            return;
        }
        List<List<Long>> lists = FlowUtils.partitionList(flowSaleIdList, 200);
        for (List<Long> list : lists) {
            // 根据流向销售id列表删除
            syncFlowSaleMapper.deleteByFlowSaleIds(list);
        }
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return null;
    }
}
