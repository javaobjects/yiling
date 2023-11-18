package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 清除状态 1-未清除，2-已清除 枚举
 * @Author fan.shen
 * @Date 2022/5/31
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcMedsClearStatusEnum {

    /**
     * 未清除
     */
    UN_CLEARED(1,"未清除"),

    /**
     * 已清除
     */
    CLEARED(2,"已清除"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcMedsClearStatusEnum getByCode(Integer code) {
        for (HmcMedsClearStatusEnum e : HmcMedsClearStatusEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
