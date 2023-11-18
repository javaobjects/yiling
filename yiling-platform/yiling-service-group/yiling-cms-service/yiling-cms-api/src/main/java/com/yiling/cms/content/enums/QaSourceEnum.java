package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 问答来源 1-内容管理
 *
 * @author: fan.shen
 * @date: 2023-03-13
 */
@Getter
@AllArgsConstructor
public enum QaSourceEnum {

    CONTENT(1, "内容管理"),

    ;

    private final Integer code;
    private final String name;
}
