package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Getter
@AllArgsConstructor
public enum OrderCouponUseReturnTypeEnum  {

    NOT_RETURN(1, "未归还"),
    RETURNED(2, "已归还"),
    ;

    private Integer code;
    private String name;

    public static OrderCouponUseReturnTypeEnum getByCode(Integer code) {
        for (OrderCouponUseReturnTypeEnum e : OrderCouponUseReturnTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
