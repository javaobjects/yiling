package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议付款方式枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementPayMethodEnum {

    /**
     * 协议付款方式
     */
    CHECK(1, "支票"),
    WIRE_TRANSFER(2, "电汇"),
    EASY_CREDIT(3, "易贷"),
    ACCEPTANCE_THREE_MONTHS(4, "3个月承兑"),
    ACCEPTANCE_SIX_MONTHS(5, "6个月承兑"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementPayMethodEnum getByCode(Integer code) {
        for (AgreementPayMethodEnum e : AgreementPayMethodEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
