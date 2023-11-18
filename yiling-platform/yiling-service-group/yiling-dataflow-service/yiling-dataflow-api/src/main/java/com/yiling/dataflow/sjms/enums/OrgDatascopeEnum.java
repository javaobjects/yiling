package com.yiling.dataflow.sjms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 机构数据范围枚举
 *
 * @author: xuan.zhou
 * @date: 2023/4/10
 */
@Getter
@AllArgsConstructor
public enum OrgDatascopeEnum {

    NONE(1, "无权限"),
    PORTION(2, "部分权限"),
    ALL(3, "全部权限"),
    ;

    private Integer code;
    private String desc;

    public static OrgDatascopeEnum getFromCode(Integer code) {
        for (OrgDatascopeEnum e : OrgDatascopeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}