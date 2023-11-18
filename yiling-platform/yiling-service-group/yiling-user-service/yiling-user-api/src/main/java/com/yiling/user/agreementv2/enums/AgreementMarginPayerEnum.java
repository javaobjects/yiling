package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保证金支付方枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementMarginPayerEnum {

    /**
     * 保证金支付方
     */
    SECOND(2, "乙方"),
    BUSINESS(3,"指定商业公司"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementMarginPayerEnum getByCode(Integer code) {
        for (AgreementMarginPayerEnum e : AgreementMarginPayerEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
