package com.yiling.framework.common.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/10
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {

    /**
     * 业务类型枚举
     */
    INSERT("insert", "新增"),
    UPDATE("update", "修改"),
    DELETE("delete", "删除"),
    IMPORT("import", "导入"),
    EXPORT("export", "导出"),
    OTHER("other", "其他"),
    ;

    private String code;
    private String name;

    public static BusinessTypeEnum getByCode(String code) {
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
