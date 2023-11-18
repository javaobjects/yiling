package com.yiling.cms.evaluate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 题目类型枚举
 *
 * @author: fan.shen
 * @date: 2022/12/07
 */
@Getter
@AllArgsConstructor
public enum QuestionTypeEnum {

    SELECT(1, "单选题"),

    BLANK(2, "填空题"),

    MULTI_SELECT(3, "多选题"),

    BMI(4, "BMI计算题"),

    SEX_SELECT(5, "性别选择题"),

    ;

    private final Integer code;

    private final String name;

    /**
     * 是否是选择题
     *
     * @param questionType
     * @return
     */
    public static boolean isSelect(Integer questionType) {
        if (SELECT.getCode().equals(questionType) || MULTI_SELECT.getCode().equals(questionType)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
