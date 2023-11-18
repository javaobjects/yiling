package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据范围枚举
 *
 * @author: xuan.zhou
 * @date: 2021/11/1
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    UNDEFINED(0, "未定义"),
    MINE(1, "本人"),
    DEPARTMENT(2, "本部门"),
    DEPARTMENT_LIST(3, "本部门及下级部门"),
    ALL(4, "全部数据"),
    ;

    private Integer code;
    private String name;

    public static DataScopeEnum getByCode(Integer code) {
        for (DataScopeEnum e : DataScopeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
