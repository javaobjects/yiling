package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流向级别枚举类
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Getter
@AllArgsConstructor
public enum ErpFlowLevelEnum {
    NO(0,"未对接"),
    YILING(1,"以岭流向"),
    ALL(2,"全品流向"),
    ;

    private Integer code;
    private String desc;

    public static ErpFlowLevelEnum getFromCode(Integer code) {
        for(ErpFlowLevelEnum e: ErpFlowLevelEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
