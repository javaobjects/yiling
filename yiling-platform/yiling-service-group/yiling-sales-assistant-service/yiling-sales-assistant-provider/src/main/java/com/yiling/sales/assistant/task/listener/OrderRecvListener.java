package com.yiling.sales.assistant.task.listener;

import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderAssistantApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 收货之后 计算任务进度和佣金
 *
 * @author: ray
 * @date: 2022/1/28
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_SA_ORDER_TASK_RATE_SEND, consumerGroup = Constants.TOPIC_SA_ORDER_TASK_RATE_SEND,tag=Constants.TAG_SA_ORDER_TASK_RATE_SEND)
public class OrderRecvListener extends AbstractMessageListener {

    @DubboReference
    private OrderApi orderApi;
    @DubboReference
    private UserApi userApi;
    @DubboReference
    private OrderDetailApi orderDetailApi;
    @DubboReference
    private OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    private OrderAssistantApi orderAssistantApi;

    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private UserTaskService userTaskService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    @MdcLog
    public MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        AddTaskOrderRequest order = marketTaskService.getOrderByNo(msg);
        if (Objects.nonNull(order)) {
            marketTaskService.handleTaskOrder(order);
            marketTaskService.newCustomerHandler(order);
            userTaskService.newUserHandler(order);
        }
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