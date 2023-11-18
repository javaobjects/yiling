package com.yiling.dataflow.statistics.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum StatisticeErrorEnum implements IErrorCode {

    FLOW_STATISTICE_ERROR(1, "统计每天商业公司流向数据异常"),
    ;

    private final Integer code;
    private final String message;

    public static StatisticeErrorEnum getFromCode(Integer code) {
        for (StatisticeErrorEnum e : StatisticeErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
