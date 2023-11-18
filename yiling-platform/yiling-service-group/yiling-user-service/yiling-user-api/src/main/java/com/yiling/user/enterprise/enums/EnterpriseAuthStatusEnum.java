package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业认证状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Getter
@AllArgsConstructor
public enum EnterpriseAuthStatusEnum {

    // 未认证
    UNAUTH(1, "未认证"),
    // 认证通过
    AUTH_SUCCESS(2, "认证通过"),
    // 认证不通过
    AUTH_FAIL(3, "认证不通过"),
    ;

    private Integer code;
    private String name;

    public static EnterpriseAuthStatusEnum getByCode(Integer code) {
        for (EnterpriseAuthStatusEnum e : EnterpriseAuthStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
