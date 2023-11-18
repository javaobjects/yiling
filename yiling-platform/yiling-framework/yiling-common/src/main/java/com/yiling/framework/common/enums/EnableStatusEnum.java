package com.yiling.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 可用状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Getter
@AllArgsConstructor
public enum EnableStatusEnum {

    ALL(0, "全部"),
    ENABLED(1, "启用"),
    DISABLED(2, "停用"),
    ;

    private Integer code;
    private String name;

    public static EnableStatusEnum getByCode(Integer code) {
        for (EnableStatusEnum e : EnableStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
