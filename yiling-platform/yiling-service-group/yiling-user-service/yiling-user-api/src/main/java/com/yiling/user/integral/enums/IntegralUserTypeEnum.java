package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-指定客户范围的用户类型枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralUserTypeEnum {

    // 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员
    ALL_USER(1, "全部用户"),
    NORMAL_USER(2, "普通用户"),
    ALL_MEMBER(3, "全部会员"),
    SOME_MEMBER(4, "部分会员"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralUserTypeEnum getByCode(Integer code) {
        for (IntegralUserTypeEnum e : IntegralUserTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
