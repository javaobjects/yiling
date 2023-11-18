package com.yiling.mall.order.mq.message.listener;

import java.util.Collections;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单已支付消息监听器 <br/>
 * 1、如果是以岭卖家的订单，则审核状态修改为待审核 <br/>
 * 2、如果是普通卖家的订单，则审核状态修改为审核通过 <br/>
 *
 * @author: xuan.zhou
 * @date: 2021/7/21
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_PAID, consumerGroup = Constants.TOPIC_ORDER_PAID)
public class OrderPaidMessageListener extends AbstractMessageListener {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String orderNo = null;
        try {
            orderNo = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);

            if (StrUtil.isEmpty(orderNo)) {
                log.error("订单号为空");
                return MqAction.CommitMessage;
            }

            OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
            if (orderDTO == null) {
                log.error("订单信息不存在：{}", orderNo);
                return MqAction.CommitMessage;
            }

            // 添加幂等防止重复消费
            if (PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.PAID) {

                log.error("订单信息订单已支付：{}", orderNo);
                return MqAction.CommitMessage;
            }

            OrderAuditStatusEnum orderAuditStatusEnum = OrderAuditStatusEnum.getByCode(orderDTO.getAuditStatus());
            if (orderAuditStatusEnum != OrderAuditStatusEnum.UNSUBMIT) {
                log.info("订单状态不满足条件：{}", orderAuditStatusEnum);
                return MqAction.CommitMessage;
            }

            if (needUpdateAuditStatus(Collections.singletonList(orderDTO.getDistributorEid()))) {

                log.info("该订单为以岭卖家订单，订单审核状态将被修改为待审核");
                orderApi.updateAuditStatus(orderDTO.getId(), OrderAuditStatusEnum.UNSUBMIT, OrderAuditStatusEnum.UNAUDIT, 0L, null);
            }

            log.info("订单已支付消息处理完毕");

        } catch (BusinessException e) {

            log.debug("[{}],errorMsg:{}",orderNo,e.getMessage());

            return MqAction.CommitMessage;

        }

        return MqAction.CommitMessage;
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


    /**
     * 是否需要修改审核状态
     * @param distributorEids
     * @return
     */
    private boolean needUpdateAuditStatus(List<Long> distributorEids) {
        // 以岭企业列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);

        if (yilingSubEids.stream().anyMatch(eid -> distributorEids.contains(eid))) {

            return true;
        }

        // 查询工业直属的企业ID信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        return industryDirectEids.stream().anyMatch(eid -> distributorEids.contains(eid));
    }

}
