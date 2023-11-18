package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2022/3/25
 */
@Getter
@AllArgsConstructor
public enum  ContentStatusEnum {

    UN_PUBLISH(1,"未发布"),
    PUBLISHED(2,"已发布"),
    ;

    private final Integer code;
    private final String name;
}