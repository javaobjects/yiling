package com.yiling.dataflow.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 ConnectStatusEnum
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Getter
@AllArgsConstructor
public enum ConnectStatusEnum {
    INVALID(0,"无效"),
    VALID(1,"有效");

    private Integer code;
    private String message;

    public static ConnectStatusEnum getByCode(Integer code) {
        for(ConnectStatusEnum e: ConnectStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
