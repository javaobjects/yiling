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
public enum HmcPolicyEndTypeEnum {

    /**
     * 保单退保
     */
    EXIT_POLICY(16, "保单退保",2,"已退保"),

    /**
     * 保险合同终止
     */
    CONTRACT_END(18, "保险合同终止",3,"已终止"),

    /**
     * 保单失效
     */
    EXPIRED(71, "保单失效",4,"已失效"),

    ;

    /**
     * 保司给以岭的type
     */
    private Integer type;

    private String  name;

    /**
     * 以岭存储的值
     */
    private Integer code;

    private String  desc;

    /**
     * 是否失效状态
     * @param type
     * @return
     */
    public static HmcPolicyEndTypeEnum match(Integer type) {
        for (HmcPolicyEndTypeEnum e : HmcPolicyEndTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
