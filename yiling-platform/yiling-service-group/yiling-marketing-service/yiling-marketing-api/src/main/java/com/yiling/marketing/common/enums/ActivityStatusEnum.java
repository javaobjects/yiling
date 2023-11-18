package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动状态枚举类
 * @author: houjie.sun
 * @date: 2021/12/20
 */
@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {

    // 1-未开始
    NOT_STARTED(1, "未开始"),
    // 2-进行中
    RUNNING(2, "进行中"),
    // 3-已结束
    END(3, "已结束"),
    ;

    private Integer code;
    private String name;

    public static ActivityStatusEnum getByCode(Integer code) {
        for (ActivityStatusEnum e : ActivityStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
