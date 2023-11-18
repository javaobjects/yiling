package com.yiling.hmc.order.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单相关错误枚举
 *
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Getter
@AllArgsConstructor
public enum HmcOrderErrorCode implements IErrorCode {

    /**
     * 订单不存在
     */
    ORDER_NOT_EXISTS(10111, "订单不存在"),

    /**
     * 订单状态不对
     */
    ORDER_STATUS_ERROR(10112, "订单状态不对"),

    /**
     * 订单配送方式不对
     */
    ORDER_DELIVER_TYPE_ERROR(10113, "订单配送方式不对"),

    /**
     * 更新订单状态失败
     */
    ORDER_UPDATE_ERROR(10114, "更新订单状态失败"),

    /**
     * 更新拿药计划状态失败
     */
    FETCH_PLAN_UPDATE_ERROR(10115, "更新拿药计划状态失败"),

    /**
     * 更新订单理赔款失败
     */
    ORDER_UPDATE_SETTLE_ERROR(10115, "更新订单理赔款失败"),

    /**
     * 保存订单处方失败
     */
    SAVE_ORDER_PRESCRIPTION_ERROR(10116, "保存订单处方失败"),

    /**
     * 保存订单处方商品失败
     */
    SAVE_ORDER_PRESCRIPTION_GOODS_ERROR(10117, "保存订单处方商品失败"),

    /**
     * 保存订单信息失败
     */
    SAVE_ORDER_ERROR(10118, "保存订单信息失败"),

    /**
     * 保存订单详情失败
     */
    SAVE_ORDER_DETAIL_ERROR(10119, "保存订单详情失败"),

    /**
     * 兑付药品库存不足
     */
    STOCK_NOT_ENOUGH_ERROR(10120, "兑付药品库存不足"),

    /**
     * 保存处方失败
     */
    SAVE_ORDER_PRESCRIPTION(10121, "保存处方失败"),

    /**
     * 订单编号不存在
     */
    ORDER_NO_NOT_EXISTS(10122, "订单编号不存在"),

    /**
     * 订单编号不存在
     */
    INSURANCE_NO_NOT_EXISTS(10123, "参保记录不存在"),

    /**
     * 此订单不满足推送保司条件
     */
    INSURANCE_ORDER_NOT_ALLOW_SYNC(10124, "此订单不满足推送保司条件"),

    /**
     * 此订单已经推送给保司了
     */
    INSURANCE_ORDER_HAVE_SYNC(10125, "此订单已经推送给保司了,请勿重复推送"),

    /**
     * 此订单处方不存在
     */
    ORDER_PRESCRIPTION_NOT_EXISTS(10126, "此订单处方不存在"),

    /**
     * 订单已经与保司同步完成，不允许修改
     */
    INSURANCE_ORDER_HAVE_SYNC_PRESCRIPTION(10127, "订单已经与保司同步完成，不允许修改"),

    /**
     * 支付状态非待支付
     */
    MARKET_ORDER_PAY_STATUS_ERROR(10128, "支付状态非待支付"),

    /**
     * 支付回调传入金额不对等
     */
    MARKET_ORDER_PAY_AMOUNT_ERROR(10129, "支付回调传入金额不对等"),

    /**
     * 处方订单异常
     */
    MARKET_ORDER_PRE_ERROR(10130, "调用IH接口创建处方订单异常"),

    /**
     * IM 获取会话缓存信息失败
     */
    IM_GET_REDIS_GROUP_ERROR(10131, "IM 获取会话缓存信息失败"),
    ;

    private final Integer code;
    private final String message;
}
