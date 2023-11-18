package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保证金返还方式枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementMarginBackTypeEnum {

    /**
     * 保证金返还方式
     */
    AGREEMENT_END(1, "协议结束后返还"),
    APPOINTED_DAY(2, "指定日期返还"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementMarginBackTypeEnum getByCode(Integer code) {
        for (AgreementMarginBackTypeEnum e : AgreementMarginBackTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
