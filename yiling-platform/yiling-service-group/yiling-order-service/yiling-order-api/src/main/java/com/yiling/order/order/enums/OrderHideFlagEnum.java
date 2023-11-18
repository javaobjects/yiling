package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单审核状态枚举
 *
 * @author: wei.wang
 * @date: 2023/02/09
 */
@Getter
@AllArgsConstructor
public enum OrderHideFlagEnum {

    SHOW(1, "显示"),
    HIDE(2, "隐藏"),
    ;

    private Integer code;
    private String name;

    public static OrderHideFlagEnum getByCode(Integer code) {
        for (OrderHideFlagEnum e : OrderHideFlagEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
