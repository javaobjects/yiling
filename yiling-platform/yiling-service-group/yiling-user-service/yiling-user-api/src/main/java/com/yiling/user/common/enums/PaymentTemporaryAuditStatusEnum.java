package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账期临时额度审核状态枚举
 *
 * @author: tingwei.chen
 * @date: 2021/7/16
 */
@Getter
@AllArgsConstructor
public enum PaymentTemporaryAuditStatusEnum {

    UNAUDIT(1, "待审核"),
    AUDIT_PASS(2, "审核通过"),
    AUDIT_REJECT(3, "审核驳回"),
    ;

    private Integer code;
    private String name;

    public static PaymentTemporaryAuditStatusEnum getByCode(Integer code) {
        for (PaymentTemporaryAuditStatusEnum e : PaymentTemporaryAuditStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
