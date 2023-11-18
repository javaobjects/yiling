package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    UNAUDITED(10, "待审核"),
    UNDELIVERED(20, "待发货"),
    PARTDELIVERED(25, "部分发货"),
    DELIVERED(30, "已发货"),
    RECEIVED(40, "已收货"),
    FINISHED(100, "已完成"),
    CANCELED(-10, "已取消")
    ;

    private Integer code;
    private String name;

    public static OrderStatusEnum getByCode(Integer code) {
        for (OrderStatusEnum e : OrderStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
