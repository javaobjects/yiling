package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 保单状态枚举类
 * @Author fan.shen
 * @Date 2021/12/20
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcPolicyStatusEnum {

    /**
     * 进行中
     */
    PROCESSING(1, "进行中"),

    /**
     * 已退保
     */
    EXIT_POLICY(2, "已退保"),

    /**
     * 已终止
     */
    CONTRACT_END(3, "已终止"),

    /**
     * 已失效
     */
    EXPIRED(4, "已失效"),

    ;

    private Integer type;

    private String  name;

    public static HmcPolicyStatusEnum getByType(Integer type) {
        for (HmcPolicyStatusEnum e : HmcPolicyStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
