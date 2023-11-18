package com.yiling.mall.order.mq.message.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderRefundDTO;
import com.yiling.order.order.dto.request.OrderRefundStatusRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.RefundStatusEnum;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.request.RefundParamListRequest;
import com.yiling.payment.pay.dto.request.RefundParamRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpStatus;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/** 退款单据自动审核监听
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_REFUND_ORDER_AUDIT_NOTIFY, consumerGroup = Constants.TOPIC_REFUND_ORDER_AUDIT_NOTIFY)
public class RefundOrderAuditMessageListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    OrderRefundApi orderRefundApi;

    @DubboReference
    RefundApi refundApi;

    @Lazy
    @Autowired
    RefundOrderAuditMessageListener _this;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String orderNo = body;
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);
        List<String> orderNoList = ListUtil.toList(StringUtils.split(body,","));

        if (CollectionUtil.isEmpty(orderNoList)) {

            log.warn("..RefundOrderAuditMessageListener:{}",orderNoList);
            return MqAction.CommitMessage;
        }

        List<OrderRefundDTO> orderRefundDTOList = orderRefundApi.listByRefundNos(orderNoList);

        if (CollectionUtil.isEmpty(orderRefundDTOList)) {

            log.warn("..RefundOrderAuditMessageListener:{}",orderRefundDTOList);
            return MqAction.CommitMessage;
        }

        // 过滤知否存在待退款的申请记录
        orderRefundDTOList = orderRefundDTOList.stream().filter(t -> RefundStatusEnum.UN_REFUND == RefundStatusEnum.getByCode(t.getRefundStatus())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(orderRefundDTOList)) {

            log.warn("..RefundOrderAuditMessageListener:{}",orderRefundDTOList);
            return MqAction.CommitMessage;
        }

        Boolean flag = _this.passRefundOrder(orderRefundDTOList);

        if (!flag) {

            return   MqAction.ReconsumeLater;
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
     * 审核通过自动生成退款记录
     * @param orderRefundDTOList
     * @param orderRefundDTOList
     * @return
     */
    @GlobalTransactional
    public boolean passRefundOrder(List<OrderRefundDTO> orderRefundDTOList) {

        // 订单支付流水记录
        List<RefundParamRequest> refundParamRequestList = orderRefundDTOList.stream().map(orderRefund -> {
            RefundParamRequest refundParam = RefundParamRequest.builder()
                    .appOrderId(orderRefund.getOrderId())
                    .refundId(orderRefund.getId())
                    .reason(orderRefund.getRemark())
                    .payNo(orderRefund.getPayNo())
                    .refundType(1) // 订单支付退款
                    .appOrderNo(orderRefund.getOrderNo())
                    .refundAmount(orderRefund.getRefundAmount()).build();
            refundParam.setOpUserId(orderRefund.getCreateUser());
            refundParam.setOpTime(orderRefund.getCreateTime());

            return refundParam;

        }).collect(Collectors.toList());

        // 调用支付生成退款记录
        Result<Void> result = refundApi.refundPayOrder(new RefundParamListRequest().setRefundParamRequestList(refundParamRequestList));

        if (HttpStatus.HTTP_OK != result.getCode()) {
            log.error("refundOrder end, result :[{}]", result);

            throw new BusinessException(OrderErrorCode.REFUND_FAIL);
        }

        // 修改订单状态为退款中
        List<OrderRefundStatusRequest> orderRefundStatusRequestList =  orderRefundDTOList.stream().map(refundDTO -> {
            OrderRefundStatusRequest orderRefundStatusRequest = new OrderRefundStatusRequest();
            orderRefundStatusRequest.setRefundId(refundDTO.getId());
            orderRefundStatusRequest.setRefundStatus(RefundStatusEnum.REFUNDING.getCode());
            orderRefundStatusRequest.setOpTime(new Date());
            return orderRefundStatusRequest;

        }).collect(Collectors.toList());

        return orderRefundApi.editStatus(orderRefundStatusRequestList);
    }
}
