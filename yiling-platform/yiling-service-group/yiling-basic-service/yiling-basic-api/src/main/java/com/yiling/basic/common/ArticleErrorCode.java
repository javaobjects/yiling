package com.yiling.basic.common;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章模块错误码枚举
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Getter
@AllArgsConstructor
public enum ArticleErrorCode implements IErrorCode {

    STATUS_ILLEGAL(10001, "文章状态非法"),

    ARTICLE_NOT_FOUND(10002, "未查询到文章"),

    ARTICLE_DISABLED(10003, "文章已停用"),
    ;

    private Integer code;
    private String message;
}
