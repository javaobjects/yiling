package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利计算规则枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateCalculateRuleEnum {

    /**
     * 返利计算规则
     */
    ACCORDING_ORDER(1, "按单计算"),
    SUMMARY(2, "汇总计算"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebateCalculateRuleEnum getByCode(Integer code) {
        for (AgreementRebateCalculateRuleEnum e : AgreementRebateCalculateRuleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
