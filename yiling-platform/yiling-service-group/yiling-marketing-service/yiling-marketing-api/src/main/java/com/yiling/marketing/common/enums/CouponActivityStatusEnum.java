package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/26
 */
@Getter
@AllArgsConstructor
public enum CouponActivityStatusEnum {

    // 1-启用
    ENABLED(1, "启用"),
    // 2-停用
    DISABLED(2, "停用"),
    // 3-废弃
    SCRAP(3, "废弃"),
    // 4-草稿 用于复制中间态
    DRAFT(4, "草稿"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityStatusEnum getByCode(Integer code) {
        for (CouponActivityStatusEnum e : CouponActivityStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
