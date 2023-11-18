package com.yiling.activity.web.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 关注来源枚举类
 * @Author fan.shen
 * @Date 2022/3/28
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SubscribeSourceEnum {

    /**
     * 自然流量
     */
    NATURE(1, "自然流量"),

    /**
     * 店员或销售
     */
    STAFF_SELLER(2, "店员或销售"),

    /**
     * 扫药盒二维码
     */
    BOX_CODE(3, "扫药盒二维码"),

    ;

    private Integer type;

    private String  name;

    public static SubscribeSourceEnum getByType(Integer type) {
        for (SubscribeSourceEnum e : SubscribeSourceEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
