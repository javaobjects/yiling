package com.yiling.cms.evaluate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 量表类型 1-健康，2-心理，3-诊疗
 *
 * @author: fan.shen
 * @date: 2022/12/07
 */
@Getter
@AllArgsConstructor
public enum HealthEvaluateTypeEnum {

    HEALTH(1, "健康"),

    PSYCHOLOGY(2, "心理"),

    DIAGNOSIS(3, "诊疗"),

    ;

    private final Integer code;

    private final String name;


}
