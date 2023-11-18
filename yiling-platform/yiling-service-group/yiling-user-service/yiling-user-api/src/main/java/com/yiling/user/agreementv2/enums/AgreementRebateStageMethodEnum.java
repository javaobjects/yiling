package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利阶梯条件计算方法枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateStageMethodEnum {

    /**
     * 返利阶梯条件计算方法
     */
    COVER(1, "覆盖计算"),
    SUPERPOSITION(2, "叠加计算"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebateStageMethodEnum getByCode(Integer code) {
        for (AgreementRebateStageMethodEnum e : AgreementRebateStageMethodEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
