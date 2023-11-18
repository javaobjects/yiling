package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author: tingwei.chen
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderReturnInvoiceStatusEnum {

    BEFORE_INVOICE(1, "开票前"),
    AFTER_INVOICE(2, "开票后");

    private Integer code;
    private String name;

    public static OrderReturnInvoiceStatusEnum getByCode(Integer code) {
        for (OrderReturnInvoiceStatusEnum e : OrderReturnInvoiceStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
