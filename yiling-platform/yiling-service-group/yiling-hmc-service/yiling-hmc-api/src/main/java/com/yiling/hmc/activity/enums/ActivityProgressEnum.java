package com.yiling.hmc.activity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动进度
 *
 * @author: fan.shen
 * @date: 2023-01-13
 */
@Getter
@AllArgsConstructor
public enum ActivityProgressEnum {

    /**
     * 未开始
     */
    UN_START(1, "未开始"),

    /**
     * 进行中
     */
    PROCESSING(2, "进行中"),

    /**
     * 已结束
     */
    ENDED(3, "已结束"),

    ;

    private final Integer code;
    private final String name;

    public static ActivityProgressEnum getByCode(Integer code) {
        for (ActivityProgressEnum e : ActivityProgressEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
