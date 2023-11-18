package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议返利兑付时间枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateCashTimeEnum {

    /**
     * 协议返利兑付时间
     */
    EFFECT_MONTH_START(1, "协议生效月起"),
    END_MONTH_START(2, "协议完结月起"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebateCashTimeEnum getByCode(Integer code) {
        for (AgreementRebateCashTimeEnum e : AgreementRebateCashTimeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
