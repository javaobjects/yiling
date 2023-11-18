package com.yiling.mall.payment.service.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.mall.order.service.OrderRefundService;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.api.OrderPaymentMethodApi;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTradeTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.RefundSourceEnum;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;

import cn.hutool.core.collection.ListUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售订单支付回调
 * @author zhigang.guo
 * @date: 2022/9/15
 */
@Component
@Slf4j
public class B2bOrderPayMsgStrategy implements MsgStrategyService<PayOrderDTO,TradeTypeEnum> {
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderRefundApi orderRefundApi;
    @DubboReference(async = true)
    MessageApi messageApi;
    @DubboReference
    OrderFirstInfoApi firstInfoApi;
    @DubboReference
    OrderPaymentMethodApi orderPaymentMethodApi;
    @DubboReference
    MqMessageSendApi      mqMessageSendApi;
    @Autowired
    private OrderRefundService orderRefundService;
    @Lazy
    @Autowired
    private B2bOrderPayMsgStrategy _this;


    @Override
    public TradeTypeEnum getMsgTradeType() {


        return TradeTypeEnum.PAY;
    }

    /**
     * 处理支付完成回调消息数据,并添加全局事务
     * @param orderDTO
     * @param payOrderDTO
     * @return
     */
    @GlobalTransactional
    public MqMessageBO processData(OrderDTO orderDTO,PayOrderDTO payOrderDTO) {
        // 如果是销售助手订单，生成消息日志
        if (OrderSourceEnum.SA == OrderSourceEnum.getByCode(orderDTO.getOrderSource())) {

            this.buildMessage(orderDTO.getCreateUser(),orderDTO.getBuyerEid(), ListUtil.toList(orderDTO));
        }

        CreateOrderPayMentMethodRequest createOrderPayMentMethodRequest = PojoUtils.map(orderDTO,CreateOrderPayMentMethodRequest.class);
        createOrderPayMentMethodRequest.setTradeType(OrderTradeTypeEnum.FULL.getCode());
        createOrderPayMentMethodRequest.setPayChannel(payOrderDTO.getPayWay());
        createOrderPayMentMethodRequest.setPaySource(payOrderDTO.getPaySource());
        createOrderPayMentMethodRequest.setPaymentTime(payOrderDTO.getPayDate());
        createOrderPayMentMethodRequest.setPaymentAmount(payOrderDTO.getPayAmount());
        createOrderPayMentMethodRequest.setOrderId(orderDTO.getId());

        // 保存支付方式
        orderPaymentMethodApi.save(createOrderPayMentMethodRequest);

        UpdateOrderPaymentStatusRequest request = new UpdateOrderPaymentStatusRequest();
        request.setOrderId(payOrderDTO.getAppOrderId());
        request.setOpUserId(payOrderDTO.getUserId());
        request.setPaymentTime(new Date());
        request.setPayWay(payOrderDTO.getPayWay());
        request.setPaySource(payOrderDTO.getPaySource());
        request.setPaymentStatus(PaymentStatusEnum.PAID.getCode());
        boolean flag =  orderApi.updatePaymentStatus(request);

        if (flag) {
            flag =  orderApi.updateOrderStatus(payOrderDTO.getAppOrderId(), OrderStatusEnum.UNAUDITED,OrderStatusEnum.UNDELIVERED,0l,"");
        }

        // 判断是否为新客
        if (flag) {
            // 判断是否新客，新客下单插入首单信息
            Boolean checkResult = firstInfoApi.checkNewVisitor(orderDTO.getBuyerEid(), OrderTypeEnum.getByCode(orderDTO.getOrderType()));
            if (checkResult) {
                flag =  firstInfoApi.saveFirstInfo(orderDTO.getId(),0l);
            }
        }

        MqMessageBO orderErpPushBo = null;
        // 发送消息
        if (flag) {
            // 通知流向埋点
            orderErpPushBo  = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(Collections.singleton(orderDTO.getId())));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
        }

        return orderErpPushBo;
    }

    @Override
    public MqAction processMessage(PayOrderDTO payOrderDTO) {

        if (log.isDebugEnabled()) {

            log.debug("销售订单支付回调:{}", payOrderDTO);
        }

        // 支付失败不用修改状态
        if (AppOrderStatusEnum.SUCCESS !=  AppOrderStatusEnum.getByCode(payOrderDTO.getAppOrderStatus())) {

            return MqAction.CommitMessage;
        }

        OrderDTO orderDTO = orderApi.getOrderInfo(payOrderDTO.getAppOrderId());
        orderDTO.setPaySource(payOrderDTO.getPaySource());

        // 添加判断，防止消息重复消费
        if (orderDTO == null || PaymentStatusEnum.PAID == PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus())) {

            log.warn("订单支付重复回调");
            return MqAction.CommitMessage;
        }
        // 系统自动创建退款单
        if (orderDTO == null || OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderDTO.getOrderStatus())) {

            return this.systemProductRefundOrder(orderDTO);
        }

        MqMessageBO orderErpPushBo =  _this.processData(orderDTO,payOrderDTO);

        if (orderErpPushBo != null) {
            mqMessageSendApi.send(orderErpPushBo);

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;

    }


    /**
     *  取消后支付，自动创建退货单
     * @param orderDTO
     * @return
     */
    private MqAction systemProductRefundOrder(OrderDTO orderDTO) {

        RefundPageRequest refundPageRequest = new RefundPageRequest();
        refundPageRequest.setSize(10);
        refundPageRequest.setCurrent(1);
        refundPageRequest.setRefundType(1);
        refundPageRequest.setOrderNo(orderDTO.getOrderNo());
        Page<PageOrderRefundDTO> pageResult = orderRefundApi.pageList(refundPageRequest);

        // 防止重复消费
        if (pageResult != null && pageResult.getTotal() > 0) {

            return MqAction.CommitMessage;
        }

        RefundOrderRequest request = new RefundOrderRequest();
        request.setBuyerEid(orderDTO.getBuyerEid());
        request.setSellerEid(orderDTO.getSellerEid());
        request.setTotalAmount(orderDTO.getTotalAmount());
        request.setPaymentAmount(orderDTO.getPaymentAmount());
        request.setOrderId(orderDTO.getId());
        request.setOrderNo(orderDTO.getOrderNo());
        request.setRefundType(1);
        request.setRefundAmount(orderDTO.getPaymentAmount());
        request.setReason("系统取消订单");
        request.setOpUserId(0l);
        request.setOpTime(new Date());
        request.setRefundSource(RefundSourceEnum.NORMAL.getCode());

        log.info("订单线取消订单退款参数RefundOrderRequest：{}", JSON.toJSONString(request));

        Boolean flag =  orderRefundService.refundOrder(request);

        if (flag) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;
    }


    /** 生成销售助手消息记录
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

        DubboUtils.quickAsyncCall("messageApi", "buildOrderListMessage");
    }
}
