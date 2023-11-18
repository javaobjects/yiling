package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举（主要服务于销售助手）
 *
 * @author xuan.zhou
 * @date 2022/1/17
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    YILING(1, "以岭人员"),
    XIAOSANYUAN(2, "小三元"),
    ZIRANREN(3, "自然人"),
    ;

    private Integer code;
    private String name;

    public static UserTypeEnum getByCode(Integer code) {
        for (UserTypeEnum e : UserTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
