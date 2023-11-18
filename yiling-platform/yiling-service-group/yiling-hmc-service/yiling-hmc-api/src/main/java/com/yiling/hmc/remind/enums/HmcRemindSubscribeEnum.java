package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 消息订阅枚举
 * @Author fan.shen
 * @Date 2022/6/17
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcRemindSubscribeEnum {

    /**
     * 接受
     */
    ACCEPT(1,"accept"),

    /**
     * 拒绝
     */
    REJECT(2,"reject"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcRemindSubscribeEnum getByCode(Integer code) {
        for (HmcRemindSubscribeEnum e : HmcRemindSubscribeEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static HmcRemindSubscribeEnum getByName(String name) {
        for (HmcRemindSubscribeEnum e : HmcRemindSubscribeEnum.values()) {
            if (name.contains(e.getName())) {
                return e;
            }
        }
        return null;
    }

}
