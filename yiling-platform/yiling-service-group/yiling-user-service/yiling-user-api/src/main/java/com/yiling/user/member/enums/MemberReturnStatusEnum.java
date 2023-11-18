package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员退款状态枚举
 *
 * @author: lun.yu
 * @date: 2022-04-15
 */
@Getter
@AllArgsConstructor
public enum MemberReturnStatusEnum {

    // 退款状态：1-待退款 2-退款中 3-退款成功 4-退款失败
    WAITING(1, "待退款"),
    RETURNING(2, "退款中"),
    SUCCESS(3, "退款成功"),
    FAIL(4, "退款失败"),
    ;

    private final Integer code;
    private final String name;

    public static MemberReturnStatusEnum getByCode(Integer code) {
        for (MemberReturnStatusEnum e : MemberReturnStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
