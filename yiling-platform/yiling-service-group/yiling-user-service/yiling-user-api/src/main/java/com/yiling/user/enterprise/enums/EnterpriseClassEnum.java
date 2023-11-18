package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业分类
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Getter
@AllArgsConstructor
public enum EnterpriseClassEnum {

    yl(1, "以岭"),

    not_yl(2, "非以岭");

    private Integer code;

    private String name;

    public static EnterpriseClassEnum getByCode(Integer code) {
        for (EnterpriseClassEnum e : EnterpriseClassEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
