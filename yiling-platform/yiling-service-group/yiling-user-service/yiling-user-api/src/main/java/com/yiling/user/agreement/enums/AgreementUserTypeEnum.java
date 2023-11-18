package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/8/28
 */
@Getter
@AllArgsConstructor
public enum AgreementUserTypeEnum {

    BUSINESS(1, "商务"),
    FINANCE(2, "财务");

    private Integer code;

    private String  name;

    public static AgreementUserTypeEnum getByCode(Integer code) {
        for (AgreementUserTypeEnum e : AgreementUserTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
