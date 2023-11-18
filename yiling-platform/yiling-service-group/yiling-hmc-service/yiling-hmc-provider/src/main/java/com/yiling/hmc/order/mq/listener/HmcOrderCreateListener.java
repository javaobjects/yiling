package com.yiling.hmc.order.mq.listener;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.hmc.insurance.service.InsuranceService;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HMC 创建订单消息监听器
 *
 * @author: fan.shen
 * @date: 2022/4/19
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_CREATE_ORDER, consumerGroup = Constants.TOPIC_HMC_CREATE_ORDER)
public class HmcOrderCreateListener extends AbstractMessageListener {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;


    @MdcLog
    @Override
    @GlobalTransactional
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[HmcOrderCreateListener]HMC监听器收到消息：{}", body);
        // 获取订单
        OrderDO orderDO = orderService.queryByOrderNo(body);
        if (!HmcOrderStatusEnum.UN_PAY.getCode().equals(orderDO.getOrderStatus())) {
            log.info("订单状态已经变更，当前状态：{}", orderDO.getOrderStatus());
            return MqAction.CommitMessage;
        }

        // 1、更新订单状态 -> 已取消
        UpdateWrapper<OrderDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(OrderDO::getId, orderDO.getId());

        OrderDO target = OrderDO.builder().orderStatus(HmcOrderStatusEnum.CANCELED.getCode()).finishTime(new Date()).build();
        orderService.update(target, updateWrapper);

        // 2、释放库存
        List<OrderDetailDO> orderDetailList = orderDetailService.listByOrderId(orderDO.getId());
        orderDetailList.forEach(item -> {
            GoodsSkuDTO goodsSkuDTO = goodsHmcApi.getGoodsSkuByGid(item.getGoodsId()).get(0);

            AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
            request.setOrderNo(orderDO.getOrderNo());
            request.setSkuId(goodsSkuDTO.getId());
            request.setInventoryId(goodsSkuDTO.getInventoryId());
            request.setFrozenQty(item.getGoodsQuantity());
            request.setOpUserId(item.getCreateUser());
            goodsHmcApi.subtractHmcFrozenQty(request);
        });

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
        };
    }
}
