package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ERP对接等级枚举
 *
 * @author: lun.yu
 * @date: 2022/01/14
 */
@Getter
@AllArgsConstructor
public enum ErpSyncLevelEnum {
    //未对接
    NOT_DOCKING(0, "未对接"),
    //基础对接
    BASED_DOCKING(1, "基础对接"),
    //订单对接
    ORDER_DOCKING(2, "订单对接"),
    //发货单对接
    DELIVERY_DOCKING(3, "发货单对接");

    private final Integer code;
    private final String name;

    public static ErpSyncLevelEnum getByCode(Integer code) {
        for (ErpSyncLevelEnum e : ErpSyncLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
