package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Getter
@AllArgsConstructor
public enum CouponActivityLogTypeEnum {

    // 1-新增
    ADD(1, "新增"),
    // 2-修改
    UPDATE(2, "修改"),
    // 3-复制
    COPY(3, "复制"),
    // 4-启用
    ENABLE(4, "启用"),
    // 4-停用
    STOP(5, "停用"),
    // 5-作废
    SCRAP(6, "作废"),
    // 6-增券
    INCREASE(7, "增券"),
    // 7-手动发放
    GIVE(8, "手动发放"),
    // 8-自动发放
    AUTO_GIVE(9, "自动发放"),
    // 9-字段领取
    AUTO_GET(10, "自主领取"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityLogTypeEnum getByCode(Integer code) {
        for (CouponActivityLogTypeEnum e : CouponActivityLogTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
