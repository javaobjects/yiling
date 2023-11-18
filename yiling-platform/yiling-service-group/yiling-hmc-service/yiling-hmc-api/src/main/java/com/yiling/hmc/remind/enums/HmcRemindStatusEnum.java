package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 提醒状态 1-有效，2-无效 枚举
 * @Author fan.shen
 * @Date 2022/5/31
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcRemindStatusEnum {

    /**
     * 有效
     */
    VALID(1,"有效"),

    /**
     * 每N小时
     */
    INVALID(2,"无效"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcRemindStatusEnum getByCode(Integer code) {
        for (HmcRemindStatusEnum e : HmcRemindStatusEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
