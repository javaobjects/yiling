package com.yiling.hmc.insurance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保险状态 1-启用 2-停用
 *
 * @author: yong.zhang
 * @date: 2022/4/8
 */
@Getter
@AllArgsConstructor
public enum InsuranceStatusEnum {

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

    public static InsuranceStatusEnum getByCode(Integer code) {
        for (InsuranceStatusEnum e : InsuranceStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
