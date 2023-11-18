package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 厂家类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/23
 */
@Getter
@AllArgsConstructor
public enum ManufacturerTypeEnum {

    /**
     * 厂家类型
     */
    PRODUCER(1, "生产厂家"),
    BRAND(2, "品牌厂家"),
    ;

    private final Integer code;
    private final String name;

    public static ManufacturerTypeEnum getByCode(Integer code) {
        for (ManufacturerTypeEnum e : ManufacturerTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
