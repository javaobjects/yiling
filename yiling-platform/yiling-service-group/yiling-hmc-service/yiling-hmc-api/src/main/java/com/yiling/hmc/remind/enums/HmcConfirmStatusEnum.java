package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 确认状态 枚举
 * @Author fan.shen
 * @Date 2022/6/1
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcConfirmStatusEnum {

    /**
     * 已确认
     */
    CONFIRMED(1,"已确认"),

    /**
     * 未确认
     */
    UN_CONFIRMED(2,"未确认"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcConfirmStatusEnum getByCode(Integer code) {
        for (HmcConfirmStatusEnum e : HmcConfirmStatusEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
