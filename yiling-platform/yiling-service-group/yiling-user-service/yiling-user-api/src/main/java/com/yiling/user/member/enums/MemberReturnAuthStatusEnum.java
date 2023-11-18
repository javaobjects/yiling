package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员退款审核状态枚举
 *
 * @author: lun.yu
 * @date: 2022-04-15
 */
@Getter
@AllArgsConstructor
public enum MemberReturnAuthStatusEnum {

    // 审核状态：1-待审核 2-已审核 3-已驳回
    WAIT(1, "待审核"),
    PASS(2, "已审核"),
    REJECT(3, "已驳回"),
    ;

    private final Integer code;
    private final String name;

    public static MemberReturnAuthStatusEnum getByCode(Integer code) {
        for (MemberReturnAuthStatusEnum e : MemberReturnAuthStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
