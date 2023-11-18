package com.yiling.sales.assistant.app.userteam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邀请用户类型枚举
 *
 * @author: lun.yu
 * @date: 2021/9/26
 */
@Getter
@AllArgsConstructor
public enum SmsInviteMemberTypeEnum {

    INVITE_MEMBER("invite_member", "邀请用户", "尊敬的用户您好，{}邀请您加入他的团队，请点击{} 进行确认"),
    ;

    private final String code;
    private final String name;
    private final String templateContent;

    public static SmsInviteMemberTypeEnum getByCode(String code) {
        for (SmsInviteMemberTypeEnum e : SmsInviteMemberTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
