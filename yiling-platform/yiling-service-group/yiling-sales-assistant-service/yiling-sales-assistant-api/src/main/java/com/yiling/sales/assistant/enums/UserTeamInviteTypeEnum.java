package com.yiling.sales.assistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团队邀请方式枚举
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/30
 */
@Getter
@AllArgsConstructor
public enum UserTeamInviteTypeEnum {

    /**
     * 团队邀请状态枚举
     */
    SMS(1, "短信"),
    WECHAT(2, "微信"),
    ;

    private final Integer code;
    private final String name;

    public static UserTeamInviteTypeEnum getByCode(Integer code) {
        for (UserTeamInviteTypeEnum e : UserTeamInviteTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
