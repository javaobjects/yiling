package com.yiling.payment.pay.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.yiling.payment.pay.dto.PayOrderDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.event
 * @date: 2021/12/31
 */
public class PayCallbackEvent  extends ApplicationEvent {

    /**
     * 支付记录
     */
    @Getter
    @Setter
    private List<PayOrderDTO> payOrderList;
    /**
     * 是否重复支付
     */
    @Getter
    @Setter
    private Boolean isRepeatPay;

    public PayCallbackEvent(Object source,  List<PayOrderDTO>  payOrderList,Boolean isRepeatPay) {
        super(source);
        this.payOrderList = payOrderList;
        this.isRepeatPay = isRepeatPay;
    }
}
