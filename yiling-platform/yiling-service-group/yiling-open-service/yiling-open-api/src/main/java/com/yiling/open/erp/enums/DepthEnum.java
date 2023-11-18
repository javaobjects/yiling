package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DepthEnum {
    NO(0,"未对接"),
    FIRST(1,"一度对接"),
    SECOND(2,"二度对接"),
    thirdly(3,"三度对接"),
    ;

    private Integer code;
    private String desc;

    public static DepthEnum getFromCode(Integer code) {
        for(DepthEnum e: DepthEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
