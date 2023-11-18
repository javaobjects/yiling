package com.yiling.cms.collect.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2022/8/1
 */
@Getter
@AllArgsConstructor
public enum CollectStatusEnums {
    COLLECTED(1,"收藏"),
    UN_COLLECTED(2,"取消收藏"),;

    private final Integer code;
    private final String name;
}
