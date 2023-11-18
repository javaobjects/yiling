package com.yiling.export.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/11/18
 */
@Getter
@AllArgsConstructor
public enum ExcelSourceEnum {

    //导入来源
    ADMIN(1, "运营后台"),
    BUSINESS(2, "商家后台"),
    FINE(3, "帆软系统前台"),
    DIH(4, "数据洞察系统");

    private Integer code;
    private String name;

    public static ExcelSourceEnum getByCode(Integer code) {
        for (ExcelSourceEnum e : ExcelSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
