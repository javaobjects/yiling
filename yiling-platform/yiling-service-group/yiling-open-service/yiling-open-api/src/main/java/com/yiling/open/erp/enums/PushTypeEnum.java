package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shuan
 */

@Getter
@AllArgsConstructor
public enum PushTypeEnum {

    ORDER_PUSH(1,"订单推送"),
    ORDER_PURCHASE_PUSH(2,"采购单推送"),
    ORDER_PURCHASE_SEND_PUSH(3,"采购发货单推送"),
    ;

    private Integer code;
    private String desc;

    public static PushTypeEnum getFromCode(Integer code) {
        for(PushTypeEnum e: PushTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
