package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分兑换类型枚举
 *
 * @author: lun.yu
 * @date: 2021/11/5
 */
@Getter
@AllArgsConstructor
public enum IntegralConvertTypeEnum {

    /**
     * 积分兑换类型
     */
    NORMAL_USER(1, "普通用户"),
    VIP(2, "会员"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralConvertTypeEnum getByCode(Integer code) {
        for (IntegralConvertTypeEnum e : IntegralConvertTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
