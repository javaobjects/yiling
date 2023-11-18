package com.yiling.cms.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Getter
@AllArgsConstructor
public enum BusinessLineEnum {
    TOC(1,"2C用户侧"),
    DOCTOR(2,"医生"),
    CLERK(3,"店员"),
    AGENT(4,"医代")
    ;
    private final Integer code;
    private final String name;
}
