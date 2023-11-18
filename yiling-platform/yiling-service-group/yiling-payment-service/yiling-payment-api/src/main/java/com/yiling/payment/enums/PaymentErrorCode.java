package com.yiling.payment.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements IErrorCode {

    PAYWAY_NOT_EXISTS(20111, "订单支付方式不存在"),
    REFUND_MONEY_ERROR(20112, "退款金额异常"),
    ORDER_NOT_EXISTS(20113, "支付订单不存在"),
    ORDER_PAID_ERROR(20114, "订单已支付成功,请勿重复支付"),
    REFUND_NOTIFY_ERROR(20115, "退款通知异常!"),
    //易宝下单失败
    YEE_CREATE_ORDER_FAIL(20116, "易宝下单失败"),
    //回调参数解密失败
    YEE_DECRYPT_FAIL(20117, "易宝回调参数解密失败"),
    //实例化私钥异常
    YEE_PRIVATE_KEY_FAIL(20118, "易宝实例化私钥异常"),
    //实例公私钥异常
    YEE_PUBLIC_KEY_FAIL(20119, "易宝实例公私钥异常"),
    //发起退款异常
    YEE_REFUND_FAIL(20120, "易宝发起退款异常"),
    //查询订单失败
    YEE_QUERY_ORDER_FAIL(20121, "易宝查询订单失败"),
    //查询退款单失败
    YEE_QUERY_REFUND_FAIL(20122, "易宝查询退款单失败"),
    //查询企业付款状态失败
    YEE_QUERY_TRANSFER_FAIL(20123, "易宝查询企业付款状态失败"),
    //易宝支付预下单支付来源非法
    YEE_PAY_SOURCE_INVALID(20124, "易宝支付预下单支付来源非法"),
    //易宝支付聚合支付预下单时微信openId不能为空
    YEE_OPEN_ID_INVALID(20125, "易宝openId不能为空"),
    //向易宝发起请求返回的response为空
    YEE_RESPONSE_IS_NULL(20126, "向易宝发起请求返回的response为空"),
    ORDER_CANCEL_ERROR(20127, "支付订单取消失败"),
    PAYMENT_ORDER_PAID_CANCEL(20128, "交易已取消，请重新发起支付!"),
    ORDER_PAID_CANCEL(20129, "订单已取消,请重新下单!"),
    USER_AUTHORIZATION_ERROR(20130, "用户授权异常,未获取到用户公众号信息!"),
    BOCOM_CREATE_ORDER_FAIL(20131, "交行下单失败"),
    BOCOM_CLOSE_ORDER_FAIL(20132, "交行关闭订单单失败"),
    BOCOM_QUERY_ORDER_FAIL(20133, "交行查询订单失败"),
    ;

    private final Integer code;
    private final String  message;
}
