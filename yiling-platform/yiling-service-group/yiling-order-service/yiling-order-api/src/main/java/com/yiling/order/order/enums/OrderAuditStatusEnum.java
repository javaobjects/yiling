package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单审核状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/7/16
 */
@Getter
@AllArgsConstructor
public enum OrderAuditStatusEnum {

    UNSUBMIT(1, "未提交"),
    UNAUDIT(2, "待审核"),
    AUDIT_PASS(3, "审核通过"),
    AUDIT_REJECT(4, "审核驳回"),
    ;

    private Integer code;
    private String name;

    public static OrderAuditStatusEnum getByCode(Integer code) {
        for (OrderAuditStatusEnum e : OrderAuditStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
