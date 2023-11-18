package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
 *
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Getter
@AllArgsConstructor
public enum HmcOrderStatusEnum {

    /**
     * 1-预订单待支付
     */
    UN_PAY(1, "预订单待支付"),
    /**
     * 2-已取消
     */
    CANCELED(2, "已取消"),
    /**
     * 3-待自提
     */
    UN_PICK_UP(3, "待自提"),
    /**
     * 4-待发货
     */
    UN_DELIVERED(4, "待发货"),
    /**
     * 5-待收货
     */
    UN_RECEIVED(5, "待收货"),
    /**
     * 6-已收货
     */
    RECEIVED(6, "已收货"),
    /**
     * 7-已完成
     */
    FINISHED(7, "已完成"),
    /**
     * 8-取消已退
     */
    RETURN_CANCELED(8, "取消已退"),
    ;

    private final Integer code;
    private final String name;

    public static HmcOrderStatusEnum getByCode(Integer code) {
        for (HmcOrderStatusEnum e : HmcOrderStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
