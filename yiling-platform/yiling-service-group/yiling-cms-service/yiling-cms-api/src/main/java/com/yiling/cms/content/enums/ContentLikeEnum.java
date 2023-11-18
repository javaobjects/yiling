package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内容点赞枚举
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@Getter
@AllArgsConstructor
public enum ContentLikeEnum {

    LIKE(1, "点赞"),
    UN_LIKE(2, "取消点赞"),
    ;

    private final Integer code;

    private final String name;
}
