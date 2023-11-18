package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 退款来源
 * @author zhigang.guo
 * @date: 2022/4/14
 */
@Getter
@AllArgsConstructor
public enum  RefundSourceEnum {

    NORMAL(1, "正常退款"),
    MEMBER(2, "会员退款");

    private Integer code;
    private String  name;

    public static RefundSourceEnum getByCode(Integer code) {
        for (RefundSourceEnum e : RefundSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
