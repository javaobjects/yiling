package com.yiling.basic.version.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Getter
@AllArgsConstructor
public enum AppTypeEnum {
    /**
     * 安卓
     */
    ANDROID(1, "安卓"),
    /**
     * ios
     */
    IOS(2, "ios"),
    ;

    private final Integer code;
    private final String name;
}
