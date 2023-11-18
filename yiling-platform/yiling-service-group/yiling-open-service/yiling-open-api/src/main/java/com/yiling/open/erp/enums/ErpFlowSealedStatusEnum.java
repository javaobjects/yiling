package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * erp流向封存状态 枚举类
 *
 * @author: houjie.sun
 * @date: 2022/4/15
 */
@Getter
@AllArgsConstructor
public enum ErpFlowSealedStatusEnum {

    UN_LOCK(1,"已解封"),
    LOCK(2,"已封存"),
            ;

    private Integer code;
    private String desc;

    public static ErpFlowSealedStatusEnum getFromCode(Integer code) {
        for(ErpFlowSealedStatusEnum e: ErpFlowSealedStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
