package com.yiling.hmc.order.mq.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.entity.MarketOrderDO;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.service.MarketOrderService;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CloseOrderRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * HMC 创建市场订单消息监听器
 *
 * @author: fan.shen
 * @date: 2023-02-17
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_CREATE_MARKET_ORDER, consumerGroup = Constants.TOPIC_HMC_CREATE_MARKET_ORDER)
public class HmcMarketOrderCreateListener extends AbstractMessageListener {

    @Autowired
    MarketOrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    PayApi payApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @MdcLog
    @Override
    @GlobalTransactional
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[HmcMarketOrderCreateListener]HMC监听器收到消息：{}", body);
        // 获取订单
        MarketOrderDTO orderDO = orderService.queryByOrderNo(body);
        if (!HmcOrderStatusEnum.UN_PAY.getCode().equals(orderDO.getOrderStatus())) {
            log.info("订单状态已经变更，当前状态：{}", orderDO.getOrderStatus());
            return MqAction.CommitMessage;
        }

        // 查询订单支付状态
        Result<Boolean> payResult = payApi.orderQueryByOrderNo(OrderPlatformEnum.HMC, TradeTypeEnum.PAY, orderDO.getOrderNo());
        if (Objects.nonNull(payResult) && !payResult.getData()) {
            // 更新订单状态 -> 已取消
            UpdateWrapper<MarketOrderDO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(MarketOrderDO::getId, orderDO.getId());

            MarketOrderDO target = MarketOrderDO.builder().orderStatus(HmcOrderStatusEnum.CANCELED.getCode()).cancelTime(DateUtil.date()).build();
            boolean result = orderService.update(target, updateWrapper);

            // 发送订单关闭消息
            if (result) {
                this.sendClosePayOrderMessage(OrderPlatformEnum.HMC, orderDO.getOrderNo());
            }
        } else {
            log.info("订单号：{}已支付成功，取消失败!", orderDO.getOrderNo());
        }

        return MqAction.CommitMessage;
    }

    /**
     * 发送订单关闭消息
     *
     * @param orderPlatformEnum
     * @param orderNo
     */
    private void sendClosePayOrderMessage(OrderPlatformEnum orderPlatformEnum, String orderNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setAppOrderNo(orderNo);
        request.setOrderPlatform(orderPlatformEnum.getCode());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, JSON.toJSONString(request));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
        };
    }
}
