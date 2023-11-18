package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分规则状态枚举
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
@Getter
@AllArgsConstructor
public enum IntegralRuleStatusEnum {

    ENABLED(1, "启用"),
    DISABLED(2, "停用"),
    DRAFT(3, "草稿"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralRuleStatusEnum getByCode(Integer code) {
        for (IntegralRuleStatusEnum e : IntegralRuleStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
