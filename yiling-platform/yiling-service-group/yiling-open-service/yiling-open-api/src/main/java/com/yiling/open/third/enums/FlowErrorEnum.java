package com.yiling.open.third.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum FlowErrorEnum implements IErrorCode {

    FLOW_BI_FTP_ERROR(1, "流向生成BI文件错误"),
    ;

    private final Integer code;
    private final String message;

    public static FlowErrorEnum getFromCode(Integer code) {
        for (FlowErrorEnum e : FlowErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
