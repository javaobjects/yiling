package com.yiling.basic.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 MailEnum
 * @描述
 * @创建时间 2022/2/10
 * @修改人 shichen
 * @修改时间 2022/2/10
 **/
@Getter
@AllArgsConstructor
public enum MailEnum {
    ORDER_PUSH_FAIL("OrderPushFailMail","订单推送erp失败提醒邮件","ErpOrderMail");
    private String code;
    private String desc;
    private String template;

    public static MailEnum getByCode(Integer code) {
        for (MailEnum e : MailEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
