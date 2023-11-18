package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员状态枚举
 *
 * @author: lun.yu
 * @date: 2022-09-30
 */
@Getter
@AllArgsConstructor
public enum MemberStatusEnum {

    // 会员状态：1-正常 2-过期
    NORMAL(1, "正常"),
    EXPIRED(2, "过期"),
    ;

    private final Integer code;
    private final String name;

    public static MemberStatusEnum getByCode(Integer code) {
        for (MemberStatusEnum e : MemberStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
