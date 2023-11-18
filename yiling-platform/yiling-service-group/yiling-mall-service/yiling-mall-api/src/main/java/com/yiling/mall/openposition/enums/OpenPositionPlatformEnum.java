package com.yiling.mall.openposition.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开屏位平台枚举
 *
 * @author: lun.yu
 * @date: 2023-06-26
 */
@Getter
@AllArgsConstructor
public enum OpenPositionPlatformEnum {

    // 平台：1-大运河 2-销售助手
    DYH(1, "大运河"),
    SA(2, "销售助手");

    private final Integer code;
    private final String name;

    public static OpenPositionPlatformEnum getByCode(Integer code) {
        for (OpenPositionPlatformEnum e : OpenPositionPlatformEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
