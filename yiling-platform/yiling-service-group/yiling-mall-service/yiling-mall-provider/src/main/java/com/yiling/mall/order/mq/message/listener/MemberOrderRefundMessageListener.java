package com.yiling.mall.order.mq.message.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.mall.order.service.OrderRefundService;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.enums.RefundSourceEnum;

import cn.hutool.core.util.CharsetUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员订单退款申请
 *
 * @author zhigang.guo
 * @date: 2022/4/14
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_MEMBER_ORDER_REFUND, consumerGroup = Constants.TOPIC_MEMBER_ORDER_REFUND)
public class MemberOrderRefundMessageListener extends AbstractMessageListener {

    @DubboReference
    OrderRefundApi orderRefundApi;
    @Autowired
    private OrderRefundService orderRefundService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Lazy
    @Autowired
    private MemberOrderRefundMessageListener    _this;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        RefundOrderRequest RefundOrderRequest = JSON.parseObject(msg, RefundOrderRequest.class);

        if (_this.createRefundOrder(RefundOrderRequest)) {
            return MqAction.CommitMessage;
        } else {
            return MqAction.ReconsumeLater;
        }

    }


    /**
     * 创建会员退款单据
     * @param RefundOrderRequest
     * @return
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public Boolean createRefundOrder(RefundOrderRequest RefundOrderRequest) {
        RefundPageRequest refundPageRequest = new RefundPageRequest();
        refundPageRequest.setSize(10);
        refundPageRequest.setCurrent(1);
        refundPageRequest.setOrderNo(RefundOrderRequest.getOrderNo());
        refundPageRequest.setRefundSource(RefundSourceEnum.MEMBER.getCode());
        Page<PageOrderRefundDTO> pageResult = orderRefundApi.pageList(refundPageRequest);
        // 判断是否重复创建，重复创建需要拦截
        if (pageResult != null && pageResult.getTotal() > 0) {
            return true;
        }

        if (orderRefundService.refundOrder(RefundOrderRequest)) {
            return true;
        }
        return false;
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
