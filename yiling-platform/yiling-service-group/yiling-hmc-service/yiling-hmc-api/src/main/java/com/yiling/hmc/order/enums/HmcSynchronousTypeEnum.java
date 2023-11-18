package com.yiling.hmc.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 与保司数据同步状态:1-待同步/2-已同步/3-同步失败（异常）
 *
 * @author: fan.shen
 * @date: 2022/7/11
 */
@Getter
@AllArgsConstructor
public enum HmcSynchronousTypeEnum {

    TO_SYNC(1, "待同步"),

    SYNCED(2, "已同步"),

    FAILED(3, "同步失败"),
    ;

    private final Integer code;
    private final String name;

    public static HmcSynchronousTypeEnum getByCode(Integer code) {
        for (HmcSynchronousTypeEnum e : HmcSynchronousTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
