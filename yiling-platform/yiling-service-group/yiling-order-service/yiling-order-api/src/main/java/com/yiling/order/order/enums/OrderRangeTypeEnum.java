package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单列表取数范围
 * @author:wei.wang
 * @date:2021/7/21
 */
@Getter
@AllArgsConstructor
public enum OrderRangeTypeEnum {
    ORDER_PURCHASE_RANGE_TYPE(1, "采购订单权限范围"),
    ORDER_SALE_ADMIN_RANGE_TYPE(2, "销售订单以岭管理员权限范围"),
    ORDER_SALE_YILINGH_RANGE_TYPE(3, "销售订单以岭普通权限范围"),
    ORDER_SALE_NOT_YILINGH_RANGE_TYPE(4, "销售订单非以岭普通权限范围"),
    ORDER_ALL_RANGE_TYPE(5, "所有权限范围")
    ;

    private Integer code;
    private String  name;

    public static OrderReturnStatusEnum getByCode(Integer code) {
        for (OrderReturnStatusEnum e : OrderReturnStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
