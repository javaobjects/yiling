package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议返利规则类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateRuleTypeEnum {

    /**
     * 协议返利规则类型
     */
    SINCE(1, "起返"),
    DOUBLE(2, "倍返"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebateRuleTypeEnum getByCode(Integer code) {
        for (AgreementRebateRuleTypeEnum e : AgreementRebateRuleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
