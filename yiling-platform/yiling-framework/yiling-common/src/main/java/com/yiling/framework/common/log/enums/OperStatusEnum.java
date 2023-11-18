package com.yiling.framework.common.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@Getter
@AllArgsConstructor
public enum OperStatusEnum {

    SUCCESS(1, "操作成功"),
    FAIL(2, "操作失败"),
    ;

    private Integer code;
    private String name;
}
