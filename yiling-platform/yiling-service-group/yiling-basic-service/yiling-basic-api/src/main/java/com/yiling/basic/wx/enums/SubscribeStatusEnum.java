package com.yiling.basic.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 公众号订阅状态枚举类
 * @Author fan.shen
 * @Date 2022/3/28
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SubscribeStatusEnum {

    /**
     * 订阅
     */
    SUBSCRIBE(1, "订阅"),

    /**
     * 取消订阅
     */
    UN_SUBSCRIBE(2, "取消订阅"),

    ;

    private Integer type;

    private String  name;

    public static SubscribeStatusEnum getByType(Integer type) {
        for (SubscribeStatusEnum e : SubscribeStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
