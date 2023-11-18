package com.yiling.cms.question.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Getter
@AllArgsConstructor
public enum QuestionErrorCode implements IErrorCode {

    QUESTION_PARAMETER_ERROR(50111, "参数不正确"),
    QUESTION_NOT_EXIST(200101, "疑问不存在")
    ;

    private final Integer code;
    private final String  message;
}
