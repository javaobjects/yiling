package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Getter
@AllArgsConstructor
public enum EnterpriseStatusEnum {
    //启用
    ENABLED(1, "启用"),
    //停用
    DISABLED(2, "停用");

    private Integer code;
    private String name;

    public static EnterpriseStatusEnum getByCode(Integer code) {
        for (EnterpriseStatusEnum e : EnterpriseStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
