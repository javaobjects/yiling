package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议生效状态枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementEffectStatusProEnum {

    /**
     * 协议生效状态：1-有效 2-中止 3-作废 4-过期 5-排期
     */
    VALID(1, "有效"),
    STOP(2, "中止"),
    INVALID(3,"作废"),
    OVERDUE(4,"过期"),
    SCHEDULING(5,"排期"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementEffectStatusProEnum getByCode(Integer code) {
        for (AgreementEffectStatusProEnum e : AgreementEffectStatusProEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
