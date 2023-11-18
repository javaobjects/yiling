package com.yiling.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *以岭环境和小程序版本对应关系
 * @author: gxl
 * @date: 2022/9/8
 */
@Getter
@AllArgsConstructor
public enum MiniAppEnvEnum {
    DEV("dev","develop"),
    TEST("test","trial"),
    PRD("prd","release"),
;
    private String selfEnv;

    private String miniAppEnv;

    public static MiniAppEnvEnum getBySelfEnv(String selfEnv) {
        for (MiniAppEnvEnum e : MiniAppEnvEnum.values()) {
            if (e.getSelfEnv().equalsIgnoreCase(selfEnv)) {
                return e;
            }
        }
        return null;
    }
}
