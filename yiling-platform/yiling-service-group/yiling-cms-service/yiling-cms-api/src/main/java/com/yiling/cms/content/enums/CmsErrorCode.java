package com.yiling.cms.content.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2022/4/29
 */
@Getter
@AllArgsConstructor
public enum CmsErrorCode implements IErrorCode {
    CONTENT_OFFLINE(11000,"文章已下线"),

    DOCUMENT_OFFLINE(110010,"文献已下架"),

    PLEASE_SELECT_CREATE_SOURCE(110020,"请选择创建来源"),

    CONTENT_NOT_FOUND(110021,"未获取到文章，请检查输入参数"),

    ;

    private final Integer code;
    private final String  message;
}