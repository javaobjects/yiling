package com.yiling.dataflow.flow.enums;

import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2023/3/27
 */
@Getter
public enum FlowEnterpriseConnectStatusEnums {

    FAIL(0, "失败"),
    SUCCESS(1, "成功")
    ;

    private Integer code;
    private String message;

    FlowEnterpriseConnectStatusEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static FlowEnterpriseConnectStatusEnums getByCode(Integer code) {
        for (FlowEnterpriseConnectStatusEnums e : FlowEnterpriseConnectStatusEnums.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
