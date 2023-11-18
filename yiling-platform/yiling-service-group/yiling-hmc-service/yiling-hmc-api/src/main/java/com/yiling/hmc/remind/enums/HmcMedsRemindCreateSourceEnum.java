package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 创建来源枚举
 * @Author fan.shen
 * @Date 2022/7/6
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcMedsRemindCreateSourceEnum {

    /**
     * 用户发起
     */
    USER_START(1, "用户发起"),

    /**
     * job发起
     */
    JOB_START(2, "job发起"),

    ;

    /**
     * 值
     */
    private Integer code;


    /**
     * 名称
     */
    private String  name;

    public static HmcMedsRemindCreateSourceEnum getByCode(Integer code) {
        for (HmcMedsRemindCreateSourceEnum e : HmcMedsRemindCreateSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
