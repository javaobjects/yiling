package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderInvoiceApplyStatusEnum {
    DEFAULT_VALUE_APPLY(0, "默认值"),
    PENDING_APPLY(1, "待申请"),
    PART_APPLIED(2, "申请中"),
    INVOICED(3, "已开票"),
    INVALID(5, "已作废"),
    NOT_NEED(6, "不需要开票"),
    ;

    private Integer code;
    private String  name;

    public static OrderInvoiceApplyStatusEnum getByCode(Integer code) {
        for (OrderInvoiceApplyStatusEnum e : OrderInvoiceApplyStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
