package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档展示状态 1-展示，2-关闭
 *
 * @author: fan.shen
 * @date: 2023-03-13
 */
@Getter
@AllArgsConstructor
public enum QaShowStatusEnum {

    show(1, "展示"),

    HIDE(2, "隐藏"),
    ;

    private final Integer code;
    private final String name;
}
