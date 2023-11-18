package com.yiling.sales.assistant.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否已读：1-未读 2-已读
 *
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Getter
@AllArgsConstructor
public enum MessageIsReadEnum {
                               /**
                                * 1-未读
                                */
                               NOT_READ(1, "未读"),
                               /**
                                * 2-已读
                                */
                               READ(2, "已读"),;

    private final Integer code;
    private final String  name;
}
