package com.yiling.mall.integral.listener;

import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.integral.dto.request.AddIntegralRequest;
import com.yiling.marketing.integral.dto.request.QueryOrderIntegralRequest;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.dto.request.AddIntegralOrderGiveRequest;
import com.yiling.user.integral.enums.IntegralPaymentMethodEnum;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B订单确认收货送积分消息 监听类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-09
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_B2B_ORDER_GIVE_INTEGRAL_SEND, consumerGroup = Constants.TOPIC_B2B_ORDER_GIVE_INTEGRAL_SEND)
public class B2bOrderIntegralListener implements MessageListener {

    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    UserIntegralApi userIntegralApi;
    @DubboReference
    OrderProcessApi orderProcessApi;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            if (StrUtil.isBlank(msg)){
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }
            QueryOrderIntegralRequest orderRequest = JSONObject.parseObject(msg, QueryOrderIntegralRequest.class);
            AddIntegralRequest request = orderProcessApi.getIntegralRecord(orderRequest.getOrderNo(), orderRequest.getOpUserId());
            log.info("B2B订单确认收货送积分消息 订单号={} 查询到的订单数据={}", orderRequest.getOrderNo(), JSONObject.toJSONString(request));
            // 支付方式转换：积分这边的支付方式：1-线上支付 2-线下支付 3-账期支付
            if (request.getPaymentMethod() == 1) {
                // 订单那边的支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
                request.setPaymentMethod(IntegralPaymentMethodEnum.OFFLINE.getCode());
            } else if (request.getPaymentMethod() == 2) {
                request.setPaymentMethod(IntegralPaymentMethodEnum.PAYMENT_DAYS.getCode());
            } else if (request.getPaymentMethod() == 4) {
                request.setPaymentMethod(IntegralPaymentMethodEnum.ONLINE.getCode());
            }
            // 查询商品信息
            AddIntegralOrderGiveRequest orderGiveRequest = PojoUtils.map(request, AddIntegralOrderGiveRequest.class);
            List<AddIntegralOrderGiveRequest.GoodsInfo> goodsInfoList = orderGiveRequest.getGoodsInfoList();
            goodsInfoList.forEach(goodsInfo -> {
                GoodsDTO goodsDTO = goodsApi.queryInfo(goodsInfo.getGoodsId());
                if (Objects.nonNull(goodsDTO)) {
                    goodsInfo.setStandardId(goodsDTO.getStandardId());
                    goodsInfo.setSellSpecificationsId(goodsDTO.getSellSpecificationsId());
                }
            });

            boolean flag = userIntegralApi.giveIntegralByOrder(orderGiveRequest);
            if (!flag){
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.ReconsumeLater;
            }
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
