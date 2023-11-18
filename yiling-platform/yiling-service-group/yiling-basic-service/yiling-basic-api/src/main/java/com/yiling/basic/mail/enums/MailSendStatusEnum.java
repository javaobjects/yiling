package com.yiling.basic.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 MailSendStatusEnum
 * @描述
 * @创建时间 2022/2/10
 * @修改人 shichen
 * @修改时间 2022/2/10
 **/
@Getter
@AllArgsConstructor
public enum MailSendStatusEnum {
    SUCCESS(1,"发送成功"),
    FAIL(2,"发送失败");

    private Integer code;
    private String message;

    public static MailSendStatusEnum getByCode(Integer code) {
        for (MailSendStatusEnum e : MailSendStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
