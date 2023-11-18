package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    //启用
    ENABLED(1, "启用"),
    //停用
    DISABLED(2, "停用"),
    //已冻结
    LOCKED(3, "已冻结"),
    //已注销
    DEREGISTER(9, "已注销"),
    ;

    private Integer code;
    private String name;

    public static UserStatusEnum getByCode(Integer code) {
        for (UserStatusEnum e : UserStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
