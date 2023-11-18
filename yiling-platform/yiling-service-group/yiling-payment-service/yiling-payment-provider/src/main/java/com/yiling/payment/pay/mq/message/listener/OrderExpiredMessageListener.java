package com.yiling.payment.pay.mq.message.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.event.PayCallbackEvent;
import com.yiling.payment.pay.service.PaymentMergeOrderService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单过期，自动关闭
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.mq.message.listener
 * @date: 2021/10/29
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_PAY_ORDER_EXPIRED, consumerGroup = Constants.TOPIC_PAY_ORDER_EXPIRED)
public class OrderExpiredMessageListener extends AbstractMessageListener implements ApplicationEventPublisherAware {
    @Autowired
    private PaymentMergeOrderService    paymentMergeOrderService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi          mqMessageConsumeFailureApi;
    private ApplicationEventPublisher   applicationEventPublisher;
    @Autowired
    private RedisService                redisService;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 设置订单redis支付状态(缓存2小时)
     * @param payId
     * @param appOrderStatusEnum
     */
    private void setOrderRedisStatus(String payId,AppOrderStatusEnum appOrderStatusEnum) {
        String   redis_order_status = RedisKey.generate("payment", "order", "payId");
        // 设置缓存支付结果
        redisService.set(redis_order_status + RedisKey.SYMBOL + payId,appOrderStatusEnum.getCode(),60 * 60 * 2 );
    }

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String payId = body;
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), payId);
        if (StringUtils.isEmpty(payId)) {
            log.error("交易ID为空");
            return MqAction.CommitMessage;
        }
        List<PaymentMergeOrderDO> resultList = paymentMergeOrderService.selectMergerOrderByPayId(payId);
        if (CollectionUtil.isEmpty(resultList)) {
            log.warn("查询结果为空");
            return MqAction.CommitMessage;
        }
        List<PaymentMergeOrderDO> waitCancelList = resultList.stream().filter(e -> AppOrderStatusEnum.WAIT_PAY == AppOrderStatusEnum.getByCode(e.getAppOrderStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(waitCancelList)) {
            return MqAction.CommitMessage;
        }

        // 设置缓存支付结果
        setOrderRedisStatus(payId,AppOrderStatusEnum.CLOSE);

        List<PaymentMergeOrderDO> updateList = Lists.newArrayList();
        List<PayOrderDTO> payOrderDTOList = Lists.newArrayList();
        for (PaymentMergeOrderDO e : waitCancelList) {
            PaymentMergeOrderDO updateOrderDo = new PaymentMergeOrderDO();
            updateOrderDo.setId(e.getId());
            updateOrderDo.setAppOrderStatus(AppOrderStatusEnum.CLOSE.getCode());
            updateList.add(updateOrderDo);
            switch (TradeTypeEnum.getByCode(e.getTradeType())) {
                // 会员购买记录，取消通知会员模块
                case MEMBER:
                    PayOrderDTO payOrderDTO = PojoUtils.map(e, PayOrderDTO.class);
                    payOrderDTO.setAppOrderStatus(AppOrderStatusEnum.CLOSE.getCode());
                    payOrderDTOList.add(payOrderDTO);
                    break;
                default:
                    break;
            }
        }

        paymentMergeOrderService.updateBatchById(updateList);

        // 会员购买记录，取消通知会员模块
        if (CollectionUtil.isNotEmpty(payOrderDTOList)) {

            this.applicationEventPublisher.publishEvent(new PayCallbackEvent(this, payOrderDTOList, false));
        }

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
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }

}
