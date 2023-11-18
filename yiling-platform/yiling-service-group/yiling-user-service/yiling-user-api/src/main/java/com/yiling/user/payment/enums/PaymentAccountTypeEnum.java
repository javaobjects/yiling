package com.yiling.user.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账期账户类型
 * @author: lun.yu
 * @date: 2021/8/27
 */
@Getter
@AllArgsConstructor
public enum PaymentAccountTypeEnum {

    // 以岭账期
    YL_PAYMENT_ACCOUNT(1, "以岭账期"),
    // 非以岭账期
    NOT_YL_PAYMENT_ACCOUNT(2, "非以岭账期"),
    // 工业直属账期
    INDUSTRY_DIRECT_PAYMENT_ACCOUNT(3, "工业直属账期");

    private final Integer code;
    private final String name;

    public static PaymentAccountTypeEnum getByCode(Integer code) {
        for (PaymentAccountTypeEnum e : PaymentAccountTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
