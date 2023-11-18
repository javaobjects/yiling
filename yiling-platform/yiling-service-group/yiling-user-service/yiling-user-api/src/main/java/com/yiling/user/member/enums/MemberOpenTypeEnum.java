package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员开通类型枚举
 *
 * @author: lun.yu
 * @date: 2022-07-15
 */
@Getter
@AllArgsConstructor
public enum MemberOpenTypeEnum {

    // 开通类型：1-首次开通 2-续费开通
    FIRST(1, "首次开通"),
    RENEWAL(2, "续费开通"),
    ;

    private final Integer code;
    private final String name;

    public static MemberOpenTypeEnum getByCode(Integer code) {
        for (MemberOpenTypeEnum e : MemberOpenTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
