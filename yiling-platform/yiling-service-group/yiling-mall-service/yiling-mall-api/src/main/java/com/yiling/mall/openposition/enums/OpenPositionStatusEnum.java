package com.yiling.mall.openposition.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开屏位状态枚举
 *
 * @author: lun.yu
 * @date: 2023-05-08
 */
@Getter
@AllArgsConstructor
public enum OpenPositionStatusEnum {

    // 状态：1-未发布 2-已发布
    NOT_RELEASE(1, "未发布"),
    HAVE_RELEASE(2, "已发布");

    private final Integer code;
    private final String name;

    public static OpenPositionStatusEnum getByCode(Integer code) {
        for (OpenPositionStatusEnum e : OpenPositionStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
