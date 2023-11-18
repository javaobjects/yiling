package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户注销账号状态枚举
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Getter
@AllArgsConstructor
public enum UserDeregisterAccountStatusEnum {

    /**
     * 注销状态：1-待注销 2-已注销 3-已撤销
     */
    WAITING_DEREGISTER(1, "待注销"),
    HAD_DEREGISTER(2, "已注销"),
    HAD_REVERT(3, "已撤销"),
    ;

    private final Integer code;
    private final String name;

    public static UserDeregisterAccountStatusEnum getByCode(Integer code) {
        for (UserDeregisterAccountStatusEnum e : UserDeregisterAccountStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
