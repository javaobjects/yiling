package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 提醒类型 每天/每N小时 枚举
 * @Author fan.shen
 * @Date 2022/5/31
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcRemindTypeEnum {

    /**
     * 每天
     */
    DAY(1,"每天"),

    /**
     * 每N小时
     */
    HOUR(2,"每N小时"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcRemindTypeEnum getByCode(Integer code) {
        for (HmcRemindTypeEnum e : HmcRemindTypeEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
