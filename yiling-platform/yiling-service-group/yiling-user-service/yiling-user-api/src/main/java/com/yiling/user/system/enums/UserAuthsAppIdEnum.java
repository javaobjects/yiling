package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户授权信息中的appId枚举
 *
 * @author: xuan.zhou
 * @date: 2022/9/23
 */
@Getter
@AllArgsConstructor
public enum UserAuthsAppIdEnum {

    // 商城
    MALL("mall", "商城"),

    // 运营后台
    ADMIN("admin", "运营后台"),

    // 健康管理中心
    HMC("hmc", "健康管理中心"),

    // 神机妙算系统
    SJMS("sjms", "神机妙算"),
    ;

    private String code;
    private String  name;

    public static UserAuthsAppIdEnum getByCode(String code) {
        for (UserAuthsAppIdEnum e : UserAuthsAppIdEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
