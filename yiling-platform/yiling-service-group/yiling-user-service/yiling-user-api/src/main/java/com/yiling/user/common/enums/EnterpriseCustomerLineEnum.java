package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业客户开通产品线枚举
 * @author lun.yu
 * @date: 2021/11/30
 */
@Getter
@AllArgsConstructor
public enum EnterpriseCustomerLineEnum {

    /**
     * 企业客户开通产品线枚举
     */
    POP(1, "POP"),
    B2B(2, "B2B"),
    ;

    private final Integer code;
    private final String name;

    public static EnterpriseCustomerLineEnum getByCode(Integer code) {
        for (EnterpriseCustomerLineEnum e : EnterpriseCustomerLineEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
