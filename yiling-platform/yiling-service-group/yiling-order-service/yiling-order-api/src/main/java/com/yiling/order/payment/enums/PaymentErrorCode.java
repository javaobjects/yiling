package com.yiling.order.payment.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author xuan.zhou
 * @date 2021/6/29
 */
@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements IErrorCode {

    ORDER_NOT_EXISTS(100000, "订单信息不存在"),
    ORDER_HAD_PAID(100001, "部分订单已经支付，请选择未支付订单"),
    NO_PAYMENT_METHOD(100002, "未设置支付方式"),
    PAYMENT_METHOD_UNUSABLE(100003, "支付方式不可用"),
    GOODS_DISCOUNT_AMOUNT_ERROR(100004, "下单商品折扣金额存在异常状态，请联系商务人员"),
    PAYMENTDAYS_QUOTA_NOT_ENOUGH(100005, "账期余额不足，请更换支付方式"),
    ;

    private final Integer code;
    private final String message;
}
