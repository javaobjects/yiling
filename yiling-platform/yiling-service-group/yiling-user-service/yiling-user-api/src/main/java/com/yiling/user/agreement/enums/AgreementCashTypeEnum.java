package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/7/12
 */
@Getter
@AllArgsConstructor
public enum AgreementCashTypeEnum {

    //协议兑付
    AGREEMENT(1, "协议兑付"),

    //手动兑付
    MANUAL(2, "手动兑付");

    private Integer code;

    private String name;

    public static AgreementCashTypeEnum getByCode(Integer code) {
        for (AgreementCashTypeEnum e : AgreementCashTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
