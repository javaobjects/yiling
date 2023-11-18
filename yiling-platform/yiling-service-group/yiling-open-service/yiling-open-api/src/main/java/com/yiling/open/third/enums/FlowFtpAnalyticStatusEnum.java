package com.yiling.open.third.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fucheng.bai
 * @date 2023/4/13
 */
@Getter
@AllArgsConstructor
public enum FlowFtpAnalyticStatusEnum {

    NOT_STARTED(1, "未开始"),
    ANALYZING(2, "解析中"),
    FINISH(3, "解析成功"),
    FAIL(4, "解析失败"),
    ;

    private final Integer code;
    private final String message;
}
