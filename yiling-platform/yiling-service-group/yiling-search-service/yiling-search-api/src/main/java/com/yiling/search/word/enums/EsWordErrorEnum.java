package com.yiling.search.word.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 EsWordErrorEnum
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Getter
@AllArgsConstructor
public enum EsWordErrorEnum implements IErrorCode {
    ERROR(200000,"扩展词库错误"),
    NOT_FOUND_TYPE(200001,"未找到该词语类型"),
    NO_SERVER_TYPE(200002,"词语类型在elasticSearch没有服务"),
    EXIST_WORD(200003,"词语已存在"),
    EMPTY_WORD(200004,"词语为空"),
    BIND_WORD(200005,"词语已绑定主词"),
    REPEAT_WORD(200006,"词语重复"),
    ;
    private final Integer code;
    private final String message;
}
