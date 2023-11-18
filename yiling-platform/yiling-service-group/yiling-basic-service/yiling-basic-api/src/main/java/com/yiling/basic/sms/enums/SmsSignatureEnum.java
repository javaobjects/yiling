package com.yiling.basic.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信签名枚举
 *
 * @author: xuan.zhou
 * @date: 2022/5/19
 */
@Getter
@AllArgsConstructor
public enum SmsSignatureEnum {

    YILING_PHARMACEUTICAL("以岭药业"),
    YILING_HEALTH("以岭健康"),
    ;

    private String name;
}
