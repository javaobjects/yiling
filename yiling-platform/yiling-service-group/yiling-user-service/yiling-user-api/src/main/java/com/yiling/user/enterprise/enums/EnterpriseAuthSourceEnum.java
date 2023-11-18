package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业审核来源枚举
 *
 * @author: lun.yu
 * @date: 2022-03-15
 */
@Getter
@AllArgsConstructor
public enum EnterpriseAuthSourceEnum {

    //B2B
    B2B(1, "B2B"),
    // 销售助手
    SA(2, "销售助手"),
    // 企业信息更新
    UPDATE(3, "企业信息更新"),
    ;

    private final Integer code;
    private final String name;

    public static EnterpriseAuthSourceEnum getByCode(Integer code) {
        for (EnterpriseAuthSourceEnum e : EnterpriseAuthSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
