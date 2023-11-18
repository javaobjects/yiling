package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-指定客户范围的企业类型枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralEnterpriseTypeEnum {

    // 指定客户范围的企业类型：1-全部类型 2-指定类型
    ALL(1, "全部类型"),
    ASSIGN(2, "指定类型"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralEnterpriseTypeEnum getByCode(Integer code) {
        for (IntegralEnterpriseTypeEnum e : IntegralEnterpriseTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
