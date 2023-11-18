package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员权益类型枚举
 *
 * @author: lun.yu
 * @date: 2021/10/26
 */
@Getter
@AllArgsConstructor
public enum MemberEquityTypeEnum {

    /**
     * 权益类型
     */
    SYSTEM_DEFINE(1, "系统定义"),
    USER_DEFINE(2, "自定义生成"),
    ;

    private final Integer code;
    private final String name;

    public static MemberEquityTypeEnum getByCode(Integer code) {
        for (MemberEquityTypeEnum e : MemberEquityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
