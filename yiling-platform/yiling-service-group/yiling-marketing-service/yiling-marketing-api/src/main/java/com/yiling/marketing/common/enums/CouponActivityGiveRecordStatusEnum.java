package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/28
 */
@Getter
@AllArgsConstructor
public enum CouponActivityGiveRecordStatusEnum {

    // 0-待发放
    WAIT(0, "待发放"),
    // 1-成功
    SUCCESS(1, "发放成功"),
    // 2-失败
    FAIL(2, "发放失败"),
    ;

    private Integer code;
    private String name;

    public static CouponActivityGiveRecordStatusEnum getByCode(Integer code) {
        for (CouponActivityGiveRecordStatusEnum e : CouponActivityGiveRecordStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
