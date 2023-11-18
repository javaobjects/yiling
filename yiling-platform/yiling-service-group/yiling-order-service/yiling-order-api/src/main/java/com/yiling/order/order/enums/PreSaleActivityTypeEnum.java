package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 预售订单活动类型
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Getter
@AllArgsConstructor
public enum  PreSaleActivityTypeEnum {

    DEPOSIT(1, "定金预售"),
    FULL(2, "全款预售"),
    ;

    private Integer code;
    private String name;

    public static PreSaleActivityTypeEnum getByCode(Integer code) {
        for (PreSaleActivityTypeEnum e : PreSaleActivityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
