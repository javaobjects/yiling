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
public enum InvoiceApplyStatusEnum {
    DEFAULT_VALUE_APPLY(0, "默认值"),
    PENDING_APPLY(1, "待申请"),
    APPLIED(2, "待开票"),
    INVOICED(3, "已开票"),
    INVALID(5, "已作废"),
    NOT_NEED(6, "不需要开票"),
    ;

    private Integer code;
    private String  name;

    public static InvoiceApplyStatusEnum getByCode(Integer code) {
        for (InvoiceApplyStatusEnum e : InvoiceApplyStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
