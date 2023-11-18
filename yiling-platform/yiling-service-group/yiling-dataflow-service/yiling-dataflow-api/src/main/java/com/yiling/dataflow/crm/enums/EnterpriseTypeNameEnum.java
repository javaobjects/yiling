package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shuan
 */

@Getter
@AllArgsConstructor
public enum EnterpriseTypeNameEnum {

    WHOLESALE(1,"批发"),
    RETAIL(2,"零售")
    ;

    private Integer code;
    private String name;

    public static EnterpriseTypeNameEnum getFromCode(Integer code) {
        for(EnterpriseTypeNameEnum e: EnterpriseTypeNameEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
