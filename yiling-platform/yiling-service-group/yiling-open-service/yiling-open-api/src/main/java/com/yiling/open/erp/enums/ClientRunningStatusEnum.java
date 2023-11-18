package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * erp接口运行状态枚举类
 */
@Getter
@AllArgsConstructor
public enum ClientRunningStatusEnum {

    OFF(1,"未运行"),
    ON(2,"运行中"),
    ;

    private Integer code;
    private String desc;

    public static ClientRunningStatusEnum getFromCode(Integer code) {
        for(ClientRunningStatusEnum e: ClientRunningStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
