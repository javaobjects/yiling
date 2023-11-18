package com.yiling.mall.payment.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

import lombok.extern.slf4j.Slf4j;

/** 定金退款回调
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Slf4j
@Component
public class DepositRefundMsgStrategy implements MsgStrategyService<RefundOrderResultDTO, TradeTypeEnum> {

    @Autowired
    private OrderRefundMsgStrategy orderRefundMsgStrategy;


    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.DEPOSIT;
    }

    @Override
    public MqAction processMessage(RefundOrderResultDTO refundOrderResultDTO) {

        if (log.isDebugEnabled()) {

            log.debug("定金订单退款回调:{}", refundOrderResultDTO);
        }

        return orderRefundMsgStrategy.processMessage(refundOrderResultDTO);
    }
}
