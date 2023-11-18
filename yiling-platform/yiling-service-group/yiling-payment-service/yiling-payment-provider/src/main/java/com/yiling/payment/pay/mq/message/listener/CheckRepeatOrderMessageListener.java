package com.yiling.payment.pay.mq.message.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.service.PaymentMergeOrderService;
import com.yiling.payment.pay.service.PaymentRepeatOrderService;
import com.yiling.payment.pay.service.PaymentTradeService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 校验订单支付是否重复，并生成重复订单
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.mq.message.listener
 * @date: 2021/10/29
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_PAY_REPEAT_NOTIFY, consumerGroup = Constants.TOPIC_ORDER_PAY_REPEAT_NOTIFY)
public class CheckRepeatOrderMessageListener implements MessageListener {

    @Autowired
    private PaymentMergeOrderService paymentMergeOrderService;

    @Autowired
    private PaymentRepeatOrderService paymentRepeatOrderService;

    @Autowired
    private PaymentTradeService paymentTradeService;

    @Override
    @MdcLog
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String payId = null;
        try {
            String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            if (StringUtils.isEmpty(msg)) {
                log.warn("交易ID为空");
                return MqAction.CommitMessage;
            }
            PayOrderDTO payOrderDTO =  JSON.parseObject(msg, PayOrderDTO.class);
            if (ObjectUtil.isNull(payOrderDTO)) {
                log.warn("通知数据为空");
                return MqAction.CommitMessage;
            }

            payId = payOrderDTO.getPayId();

            List<PaymentMergeOrderDO> resultList = paymentMergeOrderService.selectFinishMergeOrderList(payOrderDTO.getAppOrderId(),payOrderDTO.getTradeType());
            if (CollectionUtil.isEmpty(resultList)) {
                log.warn("查询结果为空");
                return MqAction.ReconsumeLater;
            }


            // 过滤当前平台的支付订单记录
            resultList = resultList.stream().filter(t -> ObjectUtil.equal(t.getOrderPlatform(),payOrderDTO.getOrderPlatform())).collect(Collectors.toList());

            if (CollectionUtil.isEmpty(resultList) || resultList.size() == 1) {

                return MqAction.CommitMessage;
            }

            // 去除当前回调的交易记录
            if (resultList.stream().filter(t -> !t.getPayId().equals(payOrderDTO.getPayId())).count() > 0) {
                PaymentMergeOrderDO paymentMergeOrderDO = resultList.stream().filter(t -> t.getPayId().equals(payOrderDTO.getPayId())).findFirst().get();
                paymentMergeOrderDO.setPayId(payOrderDTO.getPayId());
                paymentMergeOrderDO.setPayNo(payOrderDTO.getPayNo());
                // 创建重复订单
                Boolean flag = this.createRepeatOrder(paymentMergeOrderDO);
                log.info("订单号{}...createRepeatOrder..{}",paymentMergeOrderDO.getAppOrderNo(),flag);
            }
        } catch (BusinessException e) {
            log.debug("[{}],errorMsg:{}",payId,e.getMessage());
            return MqAction.CommitMessage;
        } catch (Exception e) {
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), payId, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }

    public boolean createRepeatOrder(PaymentMergeOrderDO paymentMergeOrderDO) {
        List<PaymentRepeatOrderDO> repeatOrderDOList = paymentRepeatOrderService.selectRepeatOrderByPayNo(paymentMergeOrderDO.getPayNo(),paymentMergeOrderDO.getAppOrderNo());
        // 防止创建重复，如果存在退款单，不用创建
        if (CollectionUtil.isNotEmpty(repeatOrderDOList)) {
            return true;
        }
        PaymentTradeDO  paymentTradeDO =  paymentTradeService.selectPaymentTradeByPayNo(paymentMergeOrderDO.getPayNo());
        if (paymentTradeDO == null) {
            return true;
        }

        PaymentMergeOrderDO updateMergeOrder = new PaymentMergeOrderDO();
        updateMergeOrder.setId(paymentMergeOrderDO.getId());
        updateMergeOrder.setIsDuplicate(1);

        // 标记订单为重复支付订单
        paymentMergeOrderService.updateById(updateMergeOrder);

        PaymentRepeatOrderDO paymentRepeatOrderDO = new PaymentRepeatOrderDO();
        paymentRepeatOrderDO.setAppOrderId(paymentMergeOrderDO.getAppOrderId());
        paymentRepeatOrderDO.setAppOrderNo(paymentMergeOrderDO.getAppOrderNo());
        paymentRepeatOrderDO.setRefundState(RefundStateEnum.WAIT_REFUND.getCode());
        paymentRepeatOrderDO.setRefundAmount(paymentMergeOrderDO.getPayAmount());
        paymentRepeatOrderDO.setPayId(paymentMergeOrderDO.getPayId());
        paymentRepeatOrderDO.setReason("支付重复,退款");
        paymentRepeatOrderDO.setPayNo(paymentMergeOrderDO.getPayNo());
        paymentRepeatOrderDO.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
        paymentRepeatOrderDO.setOperateTime(new Date());
        paymentRepeatOrderDO.setOperateUser(0l);
        return paymentRepeatOrderService.insertRepeatOrder(paymentRepeatOrderDO);
    }
}
