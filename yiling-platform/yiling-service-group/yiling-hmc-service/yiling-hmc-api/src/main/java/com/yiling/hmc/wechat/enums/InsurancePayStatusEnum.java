package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 保单支付状态
 *
 * @Author fan.shen
 * @Date 2022/3/30
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum InsurancePayStatusEnum {

    /**
     * 支付成功
     */
    SUCCESS(1, "支付成功"),

    /**
     * 支付失败
     */
    FAIL(2, "支付失败"),

    ;

    private Integer type;

    private String  name;

    public static InsurancePayStatusEnum getByType(Integer type) {
        for (InsurancePayStatusEnum e : InsurancePayStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
