package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-客户范围枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralCustomerScopeEnum {

    // 客户范围：1-全部客户 2-指定客户 3-指定客户范围
    ALL(1, "全部客户"),
    ASSIGN(2, "指定客户"),
    ASSIGN_SCOPE(3, "指定客户范围"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralCustomerScopeEnum getByCode(Integer code) {
        for (IntegralCustomerScopeEnum e : IntegralCustomerScopeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
