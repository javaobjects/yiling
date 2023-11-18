package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Getter
@AllArgsConstructor
public enum AgreementTaskGoalTypeEnum {

    //单品设置
    SINGLE(1, "单品设置"),
    //多品设置
    MORE(2, "多品设置");

    private Integer code;
    private String  name;
}
