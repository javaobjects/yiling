package com.yiling.mall.coupon.listener;

import java.util.concurrent.TimeUnit;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.coupon.handler.CouponAutoGiveByOrderHandler;
import com.yiling.mall.strategy.handler.StrategySendAfterOrderHandler;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动发放优惠券消息消费监听
 *
 * @author: houjie.sun
 * @date: 2021/11/22
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_COUPON_AUTOMATIC_SEND, consumerGroup = Constants.TOPIC_ORDER_COUPON_AUTOMATIC_SEND)
public class CouponAutoGiveByOrderMqListener implements MessageListener {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @Autowired
    private CouponAutoGiveByOrderHandler couponAutoGiveByOrderHandler;
    @Autowired
    RedisDistributedLock redisDistributedLock;

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @Autowired
    private StrategySendAfterOrderHandler strategySendAfterOrderHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String tags = message.getTags();
        String orderNo = null;
        String lockName = "";
        String lockId = "";
        try {
            orderNo = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);

            if (StrUtil.isBlank(orderNo)) {
                log.warn("CouponActivityAutoGiveMqListener，MsgId:{}, 订单号为空", message.getMsgId());
                return MqAction.CommitMessage;
            }
            // 订单信息
            OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
            if (orderDTO == null) {
                log.warn("CouponActivityAutoGiveMqListener，MsgId:{}, 订单信息不存在：[{}]", message.getMsgId(), orderNo);
                return MqAction.CommitMessage;
            }

            // 企业信息
            Long buyerEid = orderDTO.getBuyerEid();
            EnterpriseDTO enterprise = enterpriseApi.getById(buyerEid);
            if (ObjectUtil.isNull(enterprise)) {
                log.warn("CouponActivityAutoGiveMqListener, MsgId:{}, 企业信息不存在, orederNo:[{}]", message.getMsgId(), orderNo);
                return MqAction.CommitMessage;
            }
            lockName = RedisKey.generate("comsume_order_coupon_automatic_send", buyerEid.toString());
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            // 订单状态校验
            Integer orderStatus = orderDTO.getOrderStatus();
            //            QueryCouponActivityGiveDetailRequest request = new QueryCouponActivityGiveDetailRequest();
            // 发货后发放优惠券
            boolean isSuccess = false;
            if (Constants.TAG_ORDER_COUPON_AUTOMATIC_DELIVERED.equals(tags)) {
                if (ObjectUtil.isNull(orderStatus) || !ObjectUtil.equal(OrderStatusEnum.DELIVERED.getCode(), orderStatus)) {
                    log.warn("CouponActivityAutoGiveMqListener，MsgId:{}, 订单未发货：{}", message.getMsgId(), orderNo);
                    return MqAction.CommitMessage;
                }
                strategySendAfterOrderHandler.sendGiftAfterOrder(orderDTO.getId(), 1);

                //                request.setType(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode());
                //
                //                // 订单累计金额
                //                List<BigDecimal> orderTotalAmountList = new ArrayList<>();
                //                // 可发放活动
                //                List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList = new ArrayList<>();
                //                // 自动发券规则校验
                //                List<CouponActivityAutoGiveDetailDTO> couponActivityAutoGiveDetailDTOS = couponAutoGiveByOrderHandler.checkAccumulateAmountGiveRules(orderDTO, enterprise, request, autoGiveDetaiList, orderTotalAmountList);
                //                if (CollectionUtils.isEmpty(couponActivityAutoGiveDetailDTOS)) {
                //                    return MqAction.CommitMessage;
                //                }
                //                // 发放处理
                //                log.info("checkAccumulateAmountGiveRules" + JSON.toJSONString(autoGiveDetaiList));
                //                isSuccess = couponAutoGiveByOrderHandler.orderDeliveryHandler(request, orderDTO, enterprise, couponActivityAutoGiveDetailDTOS, orderTotalAmountList);
            } else if (Constants.TAG_ORDER_COUPON_AUTOMATIC_CREATED.equals(tags)) {
                if (ObjectUtil.isNull(orderStatus)) {
                    log.warn("CouponActivityAutoGiveMqListener，MsgId:{}, 订单未发货：{}", message.getMsgId(), orderNo);
                    return MqAction.CommitMessage;
                }
                strategySendAfterOrderHandler.sendGiftAfterOrder(orderDTO.getId(), 2);
            }

            //            log.info("自动发放优惠券消息处理完毕");
            log.info("订单发货策略满赠活动消息处理完毕");
            return MqAction.CommitMessage;
            //            if (isSuccess) {
            //                return MqAction.CommitMessage;
            //            } else {
            //                return MqAction.ReconsumeLater;
            //            }
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo, e);
            throw e;
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

}
