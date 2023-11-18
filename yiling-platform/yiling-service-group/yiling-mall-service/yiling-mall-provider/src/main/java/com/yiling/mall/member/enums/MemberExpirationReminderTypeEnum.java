package com.yiling.mall.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * B2b会员到期提醒续费文案枚举
 *
 * @author: lun.yu
 * @date: 2021/12/13
 */
@Getter
@AllArgsConstructor
public enum MemberExpirationReminderTypeEnum {

    EXPIRATION_REMINDER("expiration_reminder", "续卡提醒", "尊敬的{}您好，您的{}距离到期仅剩{}天，请尽快续费继续享受更多会员权益"),
    ;

    private String code;
    private String name;
    private String templateContent;

    public static MemberExpirationReminderTypeEnum getByCode(String code) {
        for (MemberExpirationReminderTypeEnum e : MemberExpirationReminderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
