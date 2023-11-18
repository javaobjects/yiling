package com.yiling.mall.payment.service.strategy;

import java.math.BigDecimal;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderRefundDTO;
import com.yiling.order.order.dto.request.RefundFinishRequest;
import com.yiling.order.order.enums.RefundStatusEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 普通支付订单，退款回调
 * @author zhigang.guo
 * @date: 2022/9/16
 */
@Slf4j
@Component
public class OrderRefundMsgStrategy implements MsgStrategyService<RefundOrderResultDTO,TradeTypeEnum> {

    @DubboReference
    OrderRefundApi orderRefundApi;

    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.PAY;
    }

    /**
     * 处理普通退款订单消息
     * @param payOrderDTO
     * @return
     */
    public MqAction processOrderRefundMessage(OrderRefundDTO orderRefundDTO,RefundOrderResultDTO payOrderDTO) {

        Integer refundStatus;

        switch (payOrderDTO.getRefundStateEnum()) {
            case SUCCESS:
                refundStatus = 3;
                break;
            case FALIUE:
            case CLOSE:
                refundStatus = 4;
                break;
            case REFUND_ING:
                refundStatus = 2;
                break;
            default:
                refundStatus = 1;
                break;
        }

        // 总计退款金额(原有退款金额加本次实际退款金额);
        BigDecimal realRefundAmount = NumberUtil.add(orderRefundDTO.getRealRefundAmount(),payOrderDTO.getRealReturnAmount());

        RefundFinishRequest finishRequest = new RefundFinishRequest();
        finishRequest.setRefundId(payOrderDTO.getAppRefundId());
        finishRequest.setFailReason(payOrderDTO.getErrorMessage());
        finishRequest.setRefundStatus(refundStatus);

        // 退款成功需要记录，交易流水单号，以及实际退款金额
        if (RefundStateEnum.SUCCESS == payOrderDTO.getRefundStateEnum()) {
            finishRequest.setRealRefundAmount(payOrderDTO.getRealReturnAmount());
            finishRequest.setThirdFundNo(payOrderDTO.getThirdFundNo());
            finishRequest.setRealRefundAmount(realRefundAmount);
        }

        Boolean flag = orderRefundApi.finishRefund(finishRequest);

        if (flag) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;

    }

    @Override
    public MqAction processMessage(RefundOrderResultDTO payOrderDTO) {

        if (log.isDebugEnabled()) {

            log.debug("普通订单退款回调:{}", payOrderDTO);
        }

        // 校验消息是否重复消费
        OrderRefundDTO orderRefundDTO = orderRefundApi.queryById(payOrderDTO.getAppRefundId());

        if (orderRefundDTO == null || RefundStatusEnum.REFUNDED.getCode().equals(orderRefundDTO.getRefundStatus())) {

            log.warn("订单退款重复回调");
            return MqAction.CommitMessage;
        }

        return this.processOrderRefundMessage(orderRefundDTO,payOrderDTO);
    }

}
