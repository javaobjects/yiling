package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 员工类型枚举
 *
 * @author xuan.zhou
 * @date 2022/6/6
 */
@Getter
@AllArgsConstructor
public enum EmployeeTypeEnum {

    // 商务代表
    BR(1, "商务代表"),
    // 医药代表
    MR(2, "医药代表"),
    // 企业信息更新
    OTHER(100, "其他"),
    ;

    private final Integer code;
    private final String name;

    public static EmployeeTypeEnum getByCode(Integer code) {
        for (EmployeeTypeEnum e : EmployeeTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static EmployeeTypeEnum getByName(String name) {
        for (EmployeeTypeEnum e : EmployeeTypeEnum.values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
