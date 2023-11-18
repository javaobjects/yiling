package com.yiling.user.integral.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单送积分-商家范围枚举
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Getter
@AllArgsConstructor
public enum IntegralMerchantScopeEnum {

    // 商家范围：1-全部商家 2-指定商家
    ALL(1, "全部商家"),
    ASSIGN(2, "指定商家"),
    ;

    private final Integer code;
    private final String name;

    public static IntegralMerchantScopeEnum getByCode(Integer code) {
        for (IntegralMerchantScopeEnum e : IntegralMerchantScopeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
