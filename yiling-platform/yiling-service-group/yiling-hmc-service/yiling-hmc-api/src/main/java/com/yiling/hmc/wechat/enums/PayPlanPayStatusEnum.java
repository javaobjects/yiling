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
public enum PayPlanPayStatusEnum {

    /**
     * 支付成功
     */
    SUCCESS(1, "支付成功"),

    /**
     * 待支付
     */
    WAIT(2, "待支付"),

    ;

    private Integer type;

    private String  name;

    public static PayPlanPayStatusEnum getByType(Integer type) {
        for (PayPlanPayStatusEnum e : PayPlanPayStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
