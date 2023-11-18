package com.yiling.hmc.insurance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保险服务商状态 1-启用 2-停用
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Getter
@AllArgsConstructor
public enum InsuranceCompanyStatusEnum {

    /**
     * 启用
     */
    ENABLE(1, "启用"),
    /**
     * 停用
     */
    UNABLE(2, "停用"),
    ;

    private final Integer code;
    private final String name;

    public static InsuranceCompanyStatusEnum getByCode(Integer code) {
        for (InsuranceCompanyStatusEnum e : InsuranceCompanyStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
