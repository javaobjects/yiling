package com.yiling.cms.evaluate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
 *
 * @author: fan.shen
 * @date: 2022-12-21
 */
@Getter
@AllArgsConstructor
public enum RangeTypeEnum {

    LT(1, "小于"),

    LE(2, "小于等于"),

    EQ(3, "等于"),

    GE(4, "大于等于"),

    GT(5, "大于"),

    ;

    private final Integer code;

    private final String name;


}
