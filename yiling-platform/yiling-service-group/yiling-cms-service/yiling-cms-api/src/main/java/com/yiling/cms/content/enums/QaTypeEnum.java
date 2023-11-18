package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 问答类型 1-提问，2-解答
 *
 * @author: fan.shen
 * @date: 2023-03-13
 */
@Getter
@AllArgsConstructor
public enum QaTypeEnum {

    QUESTION(1, "提问"),

    ANSWER(2, "答复"),
    ;

    private final Integer code;
    private final String name;
}
