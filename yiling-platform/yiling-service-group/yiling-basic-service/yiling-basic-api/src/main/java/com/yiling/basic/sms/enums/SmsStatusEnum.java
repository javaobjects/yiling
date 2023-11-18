package com.yiling.basic.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Getter
@AllArgsConstructor
public enum SmsStatusEnum {

    UNSENT(1, "待发送"),
    SUCCESS(2, "发送成功"),
    FAILED(3, "发送失败"),
    ;

    private Integer code;
    private String name;
}
