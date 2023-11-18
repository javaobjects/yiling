package com.yiling.marketing.presale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 状态：1-启用 2-停用 3-废弃
 *
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum GoodsPresaleEnum {

    /**
     * 1-启用
     */
    ENABLE(0, "无"),
    /**
     * 2-停用
     */
    UNABLE(1, "定金膨胀"),
    /**
     * 3-废弃
     */
    DISCARD(2, "尾款立减"),
    ;

    private Integer code;
    private String name;

    public static GoodsPresaleEnum getByType(Integer code) {
        for (GoodsPresaleEnum e : GoodsPresaleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
