package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户客户状态枚举
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/23
 */
@Getter
@AllArgsConstructor
public enum UserCustomerStatusEnum {

    /**
     * 用户客户状态枚举
     */
    WAITING(1, "待审核"),
    PASS(2, "审核通过"),
    REJECT(3,"审核驳回"),
    ;

    private final Integer code;
    private final String name;

    public static UserCustomerStatusEnum getByCode(Integer code) {
        for (UserCustomerStatusEnum e : UserCustomerStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
