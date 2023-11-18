package com.yiling.sjms.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据归档状态枚举
 * @author: gxl
 * @date: 2023/2/27
 */
@Getter
@AllArgsConstructor
public enum ArchiveStatusEnum {
    OPEN(1,"开启"),
    close(2,"关闭"),
    ;
    private Integer code;
    private String name;


    public static ArchiveStatusEnum getByCode(Integer code) {
        for (ArchiveStatusEnum e : ArchiveStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
