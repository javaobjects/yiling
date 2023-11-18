package com.yiling.hmc.address.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
@Getter
@AllArgsConstructor
public enum AddressDefaultFlagEnum {

    /**
     * 默认
     */
    IS_DEFAULT(1, "默认"),
    /**
     * 非默认
     */
    NO_DEFAULT(0, "非默认"),
    ;

    private final Integer code;
    private final String message;

    public static AddressDefaultFlagEnum getByCode(Integer code) {
        for (AddressDefaultFlagEnum e : AddressDefaultFlagEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
