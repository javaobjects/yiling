package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议返利支付方枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebatePayEnum {

    /**
     * 协议返利支付方
     */
    FIRST(1, "甲方"),
    BUSINESS(2, "指定商业公司"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebatePayEnum getByCode(Integer code) {
        for (AgreementRebatePayEnum e : AgreementRebatePayEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
