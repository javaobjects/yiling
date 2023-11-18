package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单平台类型
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderPlatformTypeEnum {

    ORDER_PLATFORM_TYPE_APP_WEB(1, "移动端前台"),
    ORDER_PLATFORM_TYPE_MALL_CENTRE(2, "商家后台-中台"),
    ORDER_PLATFORM_TYPE_MALL_B2B(3, "商家后台-B2B"),
    ORDER_PLATFORM_TYPE_ADMIN_CENTRE(4, "运营后台-中台"),
    ORDER_PLATFORM_TYPE_ADMIN_B2B(5, "运营后台-B2B")
    ;

    private Integer code;
    private String name;

    public static OrderPlatformTypeEnum getByCode(Integer code) {
        for (OrderPlatformTypeEnum e : OrderPlatformTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
