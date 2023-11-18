package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 票折方式
 *
 * @author:wei.wang
 * @date:2021/7/27
 */
@Getter
@AllArgsConstructor
public enum OrderInvoiceDiscountTypeEnum {

    USE_RATE_TYPE(1, "按比率"),
    USE_AMOUNT_TYPE(2, "按金额"),
    ;

    private Integer code;
    private String  name;

    public static OrderInvoiceDiscountTypeEnum getByCode(Integer code) {
        for (OrderInvoiceDiscountTypeEnum e : OrderInvoiceDiscountTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
