package com.yiling.dataflow.index.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum IndexErrorEnum implements IErrorCode {

    FLOW_SALE_ERROR(1, "销售流向刷新索引异常"),
    FLOW_INDEX_REFRESH_ERROR(2, "索引刷新异常"),
    ;

    private final Integer code;
    private final String message;

    public static IndexErrorEnum getFromCode(Integer code) {
        for (IndexErrorEnum e : IndexErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
