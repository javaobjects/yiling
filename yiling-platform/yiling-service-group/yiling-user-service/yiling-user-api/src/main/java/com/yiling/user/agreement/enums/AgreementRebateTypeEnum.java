package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: chen.shi
 * @date: 2021/12/29
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateTypeEnum {
    YEAR_REBATE(1,"年度返利政策"),
    TEMP_REBATE(2,"临时返利政策");

    private Integer code;

    private String name;

    public static AgreementRebateTypeEnum getByCode(Integer code) {
        for (AgreementRebateTypeEnum e : AgreementRebateTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
