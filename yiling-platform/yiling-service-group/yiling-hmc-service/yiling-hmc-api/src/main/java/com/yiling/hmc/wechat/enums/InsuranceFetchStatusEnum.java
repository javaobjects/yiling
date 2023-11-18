package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 拿药状态枚举类
 * 1-已拿，2-未拿
 * @Author fan.shen
 * @Date 2022/3/28
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum InsuranceFetchStatusEnum {

    /**
     * 已拿
     */
    TOOK(1, "已拿"),

    /**
     * 未拿
     */
    WAIT(2, "未拿"),

    /**
     * 已申请待拿
     */
    APPLY_TAKE(3, "已申请待拿")
    ;

    private Integer type;

    private String  name;

    public static InsuranceFetchStatusEnum getByType(Integer type) {
        for (InsuranceFetchStatusEnum e : InsuranceFetchStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
