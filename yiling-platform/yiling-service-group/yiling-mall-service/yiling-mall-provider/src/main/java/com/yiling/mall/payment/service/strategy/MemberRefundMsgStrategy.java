package com.yiling.mall.payment.service.strategy;

import java.util.EnumSet;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderRefundDTO;
import com.yiling.order.order.enums.RefundStatusEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.request.UpdateMemberReturnStatusRequest;
import com.yiling.user.member.enums.MemberReturnStatusEnum;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员退款回调
 *
 * @author zhigang.guo
 * @date: 2022/9/16
 */
@Component
@Slf4j
public class MemberRefundMsgStrategy implements MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum> {

    @DubboReference
    OrderRefundApi orderRefundApi;
    @DubboReference
    MemberReturnApi memberReturnApi;
    @Autowired
    private OrderRefundMsgStrategy orderRefundMsgStrategy;
    @Lazy
    @Autowired
    private MemberRefundMsgStrategy _this;

    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.MEMBER;
    }

    /**
     * 处理会员退款数据，并添加全局事务
     * @param orderRefundDTO
     * @param payOrderDTO
     * @return
     */
    @GlobalTransactional
    public MqAction processData(OrderRefundDTO orderRefundDTO, RefundOrderResultDTO payOrderDTO) {

        // 退款成功&&退款失败
        MqAction mqAction = orderRefundMsgStrategy.processOrderRefundMessage(orderRefundDTO,payOrderDTO);

        if (MqAction.ReconsumeLater == mqAction) {

            return mqAction;
        }

        // 会员退款订单只需通知退款成功或者退款失败
        EnumSet<RefundStateEnum> refundStateEnumEnum = EnumSet.of(RefundStateEnum.SUCCESS, RefundStateEnum.FALIUE, RefundStateEnum.CLOSE);
        // 表示会员退款订单，需要调整会员退款记录状态
        if (!refundStateEnumEnum.contains(payOrderDTO.getRefundStateEnum())) {

            return mqAction;
        }

        UpdateMemberReturnStatusRequest request = new UpdateMemberReturnStatusRequest();

        if (RefundStateEnum.SUCCESS == payOrderDTO.getRefundStateEnum()) {
            request.setReturnStatus(MemberReturnStatusEnum.SUCCESS.getCode());
        } else {
            request.setReturnStatus(MemberReturnStatusEnum.FAIL.getCode());
        }

        request.setMemberReturnId(orderRefundDTO.getReturnId());
        Boolean result = memberReturnApi.updateReturnStatus(request);

        if (!result) {

            throw new RuntimeException("更新会员订单异常!");
        }

        return mqAction;
    }

    @Override
    public MqAction processMessage(RefundOrderResultDTO payOrderDTO) {

        if (log.isDebugEnabled()) {

            log.debug("会员订单支付回调:{}", payOrderDTO);
        }

        // 校验消息是否重复消费
        OrderRefundDTO orderRefundDTO = orderRefundApi.queryById(payOrderDTO.getAppRefundId());
        if (orderRefundDTO == null || RefundStatusEnum.REFUNDED.getCode().equals(orderRefundDTO.getRefundStatus())) {
            log.warn("订单退款重复回调");
            return MqAction.CommitMessage;
        }

        return _this.processData(orderRefundDTO,payOrderDTO);
    }
}
