package com.yiling.sales.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团队邀请状态枚举
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Getter
@AllArgsConstructor
public enum UserTeamStatusEnum {

    /**
     * 团队邀请状态枚举
     */
    WAITING(0, "未注册"),
    PASS(1, "已注册"),
    ;

    private final Integer code;
    private final String name;

    public static UserTeamStatusEnum getByCode(Integer code) {
        for (UserTeamStatusEnum e : UserTeamStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
