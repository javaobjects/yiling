package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区分用户身份枚举
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
@Getter
@AllArgsConstructor
public enum IntegralUserFlagEnum {

    // 是否区分用户身份：1-全部 2-指定会员类型
    ALL(1, "全部"),
    ASSIGN(2, "指定会员类型"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralUserFlagEnum getByCode(Integer code) {
        for (IntegralUserFlagEnum e : IntegralUserFlagEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
