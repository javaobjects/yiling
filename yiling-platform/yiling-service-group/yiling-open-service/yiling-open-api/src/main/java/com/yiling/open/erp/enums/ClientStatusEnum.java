package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientStatusEnum {

    OUT(0,"关闭"),
    IN(1,"开启"),
    ;

    private Integer code;
    private String desc;

    public static ClientStatusEnum getFromCode(Integer code) {
        for(ClientStatusEnum e: ClientStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
