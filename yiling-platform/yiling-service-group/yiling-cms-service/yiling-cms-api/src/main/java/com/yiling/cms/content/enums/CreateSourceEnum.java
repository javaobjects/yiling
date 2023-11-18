package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 创建来源枚举
 * @author: fan.shen
 * @date: 2022-11-10
 */
@Getter
@AllArgsConstructor
public enum CreateSourceEnum {
    
    ADMIN(1,"运营后台"),

    IH_ADMIN(2,"IH后台"),

    ;

    private final Integer code;
    private final String name;
}
