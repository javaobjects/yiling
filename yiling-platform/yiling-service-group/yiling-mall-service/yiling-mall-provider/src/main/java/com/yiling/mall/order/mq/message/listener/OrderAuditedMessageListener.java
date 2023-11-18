package com.yiling.mall.order.mq.message.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单已审核消息监听器 <br/>
 *
 * @author: xuan.zhou
 * @date: 2021/7/21
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_AUDITED, consumerGroup = Constants.TOPIC_ORDER_AUDITED)
public class OrderAuditedMessageListener extends AbstractMessageListener {
    @DubboReference
    OrderApi              orderApi;
    @DubboReference
    OrderDetailApi        orderDetailApi;
    @DubboReference
    InventoryApi          inventoryApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    MessageApi            messageApi;
    @DubboReference
    OrderFirstInfoApi     firstInfoApi;
    @DubboReference
    MqMessageSendApi      mqMessageSendApi;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Lazy
    @Autowired
    private OrderAuditedMessageListener     _this;

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

            OrderAuditStatusEnum orderAuditStatusEnum = OrderAuditStatusEnum.getByCode(orderDTO.getAuditStatus());

            if (orderAuditStatusEnum == OrderAuditStatusEnum.AUDIT_PASS) {
                return this.processAuditedPass(orderDTO);
            } else if (orderAuditStatusEnum == OrderAuditStatusEnum.AUDIT_REJECT) {
                return _this.processAuditedReject(orderDTO);
            }
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
     * 处理自动审核数据
     * @param orderDTO
     * @return
     */
    @GlobalTransactional
    public MqMessageBO processAuditedData(OrderDTO orderDTO)  {

        // 修改订单状态为“待发货”
        orderApi.updateOrderStatus(orderDTO.getId(), OrderStatusEnum.UNAUDITED, OrderStatusEnum.UNDELIVERED, orderDTO.getAuditUser(), null);

        // 判断是否新客，新客下单插入首单信息
        if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(orderDTO.getOrderType())) {
            Boolean checkResult = firstInfoApi.checkNewVisitor(orderDTO.getBuyerEid(), OrderTypeEnum.getByCode(orderDTO.getOrderType()));
            if (checkResult) {
                firstInfoApi.saveFirstInfo(orderDTO.getId(),0l);
            }
        }

        // 流向信息
        MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderDTO.getId())));

        orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);

        // 如果是销售助手订单，生成消息日志
        if (OrderSourceEnum.SA == OrderSourceEnum.getByCode(orderDTO.getOrderSource())) {
            this.buildMessage(orderDTO.getCreateUser(),orderDTO.getBuyerEid(), ListUtil.toList(orderDTO));
        }

        return orderErpPushBo;
    }

    /**
     * 处理订单审核成功
     * @param orderDTO
     * @return
     */
    public MqAction processAuditedPass(OrderDTO orderDTO) {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getByCode(orderDTO.getOrderStatus());
        if (orderStatusEnum != OrderStatusEnum.UNAUDITED) {
            log.error("订单状态不满足条件：{}", orderStatusEnum);
            return MqAction.CommitMessage;
        }

        // 处理自动审核通过数据
        MqMessageBO mqMessageBO = _this.processAuditedData(orderDTO);

        // 发送流向消息
        mqMessageSendApi.send(mqMessageBO);

        log.info("订单审核通过消息处理完毕");
        return MqAction.CommitMessage;
    }

    /**
     * 创建待发货，发送消息
     * @param userId    用户id
     * @param eid   企业id
     * @param orderDTOList  订单信息
     */
    private void buildMessage(Long userId, Long eid, List<OrderDTO> orderDTOList) {
        OrderListMessageBuildRequest request = new OrderListMessageBuildRequest();
        request.setUserId(userId);
        request.setEid(eid);
        request.setSourceEnum(SourceEnum.SA);
        request.setMessageRoleEnum(MessageRoleEnum.TO_USER);
        request.setMessageNodeEnum(MessageNodeEnum.WAITING_SHIPMENT);
        List<OrderMessageRequest> orderMessageRequestList = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList) {
            orderMessageRequestList.add(new OrderMessageRequest().setOrderNo(orderDTO.getOrderNo()).setCode(orderDTO.getOrderNo()).setCreateTime(orderDTO.getCreateTime()));
        }
        request.setOrderMessageRequestList(orderMessageRequestList);
        messageApi.buildOrderListMessage(request);
    }

    /**
     * 处理订单驳回消息
     * @param orderDTO
     * @return
     */
    @GlobalTransactional
    public MqAction processAuditedReject(OrderDTO orderDTO) {
        PaymentMethodEnum orderPaymentMethodEnum = PaymentMethodEnum.getByCode(orderDTO.getPaymentMethod().longValue());

        // 账期订单驳回时，退还账期额度
        if (orderPaymentMethodEnum == PaymentMethodEnum.PAYMENT_DAYS) {
            RefundPaymentDaysAmountRequest request = new RefundPaymentDaysAmountRequest();
            request.setOrderId(orderDTO.getId());
            request.setRefundAmount(orderDTO.getPaymentAmount());
            request.setPlatformEnum(PlatformEnum.getByCode(orderDTO.getOrderType()));
            request.setOpUserId(orderDTO.getAuditUser());
            paymentDaysAccountApi.refund(request);
        }

        // 释放库存
        //List<Long> gids = new ArrayList<>();
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
        orderDetailDTOList.forEach(orderDetailDTO -> {
            AddOrSubtractQtyRequest addOrSubtractQtyRequest = new AddOrSubtractQtyRequest();
            addOrSubtractQtyRequest.setSkuId(orderDetailDTO.getGoodsSkuId());
            addOrSubtractQtyRequest.setFrozenQty(orderDetailDTO.getGoodsQuantity().longValue());
            addOrSubtractQtyRequest.setOpUserId(orderDTO.getAuditUser());
            addOrSubtractQtyRequest.setOrderNo(orderDTO.getOrderNo());
            inventoryApi.subtractFrozenQty(addOrSubtractQtyRequest);
            //gids.add(orderDetailDTO.getDistributorGoodsId());
        });
        /*if(CollectionUtil.isNotEmpty(gids)){
            inventoryApi.sendInventoryMessage(gids);
        }*/

        log.info("订单审核驳回消息处理完毕");
        return MqAction.CommitMessage;
    }
}
