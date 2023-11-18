package com.yiling.dataflow.gb.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum GbErrorEnum implements IErrorCode {
    GB_SUBTRACT_ERROR(1, "团购扣减异常"),
    SELECT_FLOW_ERROR(2, "选择源流向保存异常"),
    DELETE_MATCH_FLOW_ERROR(3, "源流向匹配删除异常"),
    DELETE_FLOW_SALE_REPORT_ERROR(4, "删除销售报表团购流向异常"),
    ;

    private final Integer code;
    private final String message;

    public static GbErrorEnum getFromCode(Integer code) {
        for (GbErrorEnum e : GbErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
