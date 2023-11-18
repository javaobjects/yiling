package com.yiling.sales.assistant.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息来源
 *
 * @author: yong.zhang
 * @date: 2021/9/18
 */
@Getter
@AllArgsConstructor
public enum SourceEnum {
    POP(1, "POP"),
    SA(2, "销售助手"),
    B2B(3, "B2B"),
    ;

    private Integer code;
    private String  name;
}
