package com.yiling.mall.payment.mq.message.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 大运河在线支付回调通知监听
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.mq.message.listener
 * @date: 2021/10/29
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_PAY_NOTIFY, consumerGroup = Constants.TOPIC_ORDER_PAY_NOTIFY)
public class OrderPayFinishMessageListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Resource
    @Qualifier("b2bOrderPayMsgStrategy")
    private MsgStrategyService<PayOrderDTO, TradeTypeEnum> orderPayMsgStrategy;

    @Resource
    @Qualifier("memberPayMsgStrategy")
    private MsgStrategyService<PayOrderDTO,TradeTypeEnum> memberPayMsgStrategy;

    @Resource
    @Qualifier("paymentDaysOrderMsgStrategy")
    private MsgStrategyService<PayOrderDTO,TradeTypeEnum>  paymentDaysOrderMsgStrategy;

    @Resource
    @Qualifier("depositPayMsgStrategy")
    private MsgStrategyService<PayOrderDTO,TradeTypeEnum>  depositPayMsgStrategy;

    @Resource
    @Qualifier("balancePayMsgStrategy")
    private MsgStrategyService<PayOrderDTO,TradeTypeEnum>  balancePayMsgStrategy;

    /**
     * 支付回调转发消息策略
     */
    private final Map<Integer, MsgStrategyService<PayOrderDTO,TradeTypeEnum>> MSG_TRANSMIT_STRATEGY_MAP = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        // 大运河：商城订单支付,购买会员,账期,定金,尾款
        MSG_TRANSMIT_STRATEGY_MAP.put(orderPayMsgStrategy.getMsgTradeType().getCode(), orderPayMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(memberPayMsgStrategy.getMsgTradeType().getCode(), memberPayMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(paymentDaysOrderMsgStrategy.getMsgTradeType().getCode(), paymentDaysOrderMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(depositPayMsgStrategy.getMsgTradeType().getCode(), depositPayMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(balancePayMsgStrategy.getMsgTradeType().getCode(), balancePayMsgStrategy);
    }


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        PayOrderDTO payOrderDTO = JSON.parseObject(msg, PayOrderDTO.class);

        if (ObjectUtil.isNull(payOrderDTO)) {
            log.error("通知数据为空");
            return MqAction.CommitMessage;
        }

        // 防止消息重复，出现重复回调
        String lockName = "payment" + Constants.SEPARATOR_MIDDLELINE + "callback" + Constants.SEPARATOR_MIDDLELINE + payOrderDTO.getAppOrderNo();
        String lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);

        try {
            MsgStrategyService msgStrategyService = MSG_TRANSMIT_STRATEGY_MAP.get(payOrderDTO.getTradeType());

            if (msgStrategyService == null) {

                throw new RuntimeException("支付回调失败,交易类型错误!");
            }

            return msgStrategyService.processMessage(payOrderDTO);

        }  finally {

            redisDistributedLock.releaseLock(lockName, lockId);
        }

    }

    @Override
    protected int getMaxReconsumeTimes() {

        return 7;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {

        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }

}