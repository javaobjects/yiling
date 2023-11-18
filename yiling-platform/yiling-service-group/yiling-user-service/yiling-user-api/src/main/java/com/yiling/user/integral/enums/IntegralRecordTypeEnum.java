package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 积分发放/扣减记录类型枚举
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
@Getter
@AllArgsConstructor
public enum IntegralRecordTypeEnum {

    // 积分发放/扣减记录类型：1-发放 2-扣减
    GIVE(1, "发放"),
    USE(2, "扣减"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralRecordTypeEnum getByCode(Integer code) {
        for (IntegralRecordTypeEnum e : IntegralRecordTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
