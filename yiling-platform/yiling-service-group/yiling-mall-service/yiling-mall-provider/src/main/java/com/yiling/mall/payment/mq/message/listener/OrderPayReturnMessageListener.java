package com.yiling.mall.payment.mq.message.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 在线退款回调监听
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.mq.message.listener
 * @date: 2021/10/29
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_PAY_REFUND_NOTIFY, consumerGroup = Constants.TOPIC_ORDER_PAY_REFUND_NOTIFY)
public class OrderPayReturnMessageListener extends AbstractMessageListener {
    /**
     * 支付退款回调转发消息策略
     */
    private final Map<Integer, MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum>> MSG_TRANSMIT_STRATEGY_MAP = new ConcurrentHashMap<>();

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Resource
    @Qualifier("orderRefundMsgStrategy")
    private MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum> orderRefundMsgStrategy;

    @Resource
    @Qualifier("memberRefundMsgStrategy")
    private MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum> memberRefundMsgStrategy;

    @Resource
    @Qualifier("depositRefundMsgStrategy")
    private MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum> depositRefundMsgStrategy;

    @Resource
    @Qualifier("balanceRefundMsgStrategy")
    private MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum> balanceRefundMsgStrategy;


    @PostConstruct
    public void init() {
        MSG_TRANSMIT_STRATEGY_MAP.put(orderRefundMsgStrategy.getMsgTradeType().getCode(), orderRefundMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(memberRefundMsgStrategy.getMsgTradeType().getCode(), memberRefundMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(depositRefundMsgStrategy.getMsgTradeType().getCode(), depositRefundMsgStrategy);
        MSG_TRANSMIT_STRATEGY_MAP.put(balanceRefundMsgStrategy.getMsgTradeType().getCode(), balanceRefundMsgStrategy);
    }


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");;

        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        RefundOrderResultDTO payOrderDTO = JSON.parseObject(msg, RefundOrderResultDTO.class);

        if (ObjectUtil.isNull(payOrderDTO) || ObjectUtil.isNull(payOrderDTO.getRefundStateEnum())) {
            log.error("通知数据为空");
            return MqAction.CommitMessage;
        }

        MsgStrategyService msgStrategyService = MSG_TRANSMIT_STRATEGY_MAP.get(payOrderDTO.getTradeTypeEnum().getCode());

        if (msgStrategyService == null) {

            throw new RuntimeException("退款回调失败,交易类型错误!");
        }

        // 退款申请处理
        return msgStrategyService.processMessage(payOrderDTO);
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
