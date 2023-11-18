package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2023/3/6
 */
@Getter
@AllArgsConstructor
public enum CollectionMethodEnum {
    //1-工具 2-ftp 3-第三方接口 4-以岭接口 5-excel导入
    TOOL(1, "工具"),
    FTP(2, "ftp"),

    THIRD_INTERFACE(3, "第三方接口"),
    YL_INTERFACE(4, "以岭接口"),
    EXCEL(5, "excel导入"),
    SYSTEM_IMPORT(6, "系统导入"),
    ;

    private Integer code;
    private String  desc;

    public static CollectionMethodEnum getByCode(Integer code) {
        for(CollectionMethodEnum e: CollectionMethodEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
