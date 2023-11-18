package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户授权信息中的identity_type枚举
 *
 * @author: xuan.zhou
 * @date: 2022/9/23
 */
@Getter
@AllArgsConstructor
public enum UserAuthsIdentityTypeEnum {

    // 用户名
    USERNAME("username", "用户名"),

    // 手机号
    MOBILE("mobile", "手机号"),

    // 邮箱
    EMAIL("email", "邮箱"),

    // 微信
    WEIXIN("weixin", "微信"),

    // 支付宝
    ALIPAY("alipay", "支付宝"),
    ;

    private String code;
    private String  name;

    public static UserAuthsIdentityTypeEnum getByCode(String code) {
        for (UserAuthsIdentityTypeEnum e : UserAuthsIdentityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
