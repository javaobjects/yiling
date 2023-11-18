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
public enum AgreementEffectStatusEnum {

    /**
     * 协议生效状态
     */
    NORMAL(1, "正常"),
    STOP(2, "中止"),
    INVALID(3,"作废"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementEffectStatusEnum getByCode(Integer code) {
        for (AgreementEffectStatusEnum e : AgreementEffectStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
