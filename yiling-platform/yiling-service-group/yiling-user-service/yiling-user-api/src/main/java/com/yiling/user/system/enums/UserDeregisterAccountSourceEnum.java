package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户注销账号来源枚举
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Getter
@AllArgsConstructor
public enum UserDeregisterAccountSourceEnum {

    /**
     * 来源：1-销售助手APP 2-大运河APP
     */
    SALES_ASSISTANT_APP(1, "销售助手APP"),
    B2B_APP(2, "大运河APP"),
    DOCTOR_ASSISTANT_APP(3, "医生助手APP"),
    ;

    private final Integer code;
    private final String name;

    public static UserDeregisterAccountSourceEnum getByCode(Integer code) {
        for (UserDeregisterAccountSourceEnum e : UserDeregisterAccountSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
