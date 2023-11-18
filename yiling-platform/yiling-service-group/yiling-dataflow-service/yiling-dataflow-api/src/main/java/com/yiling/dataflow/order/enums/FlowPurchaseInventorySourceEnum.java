package com.yiling.dataflow.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流向库存，采购来源枚举类
 *
 * @author: houjie.sun
 * @date: 2022/5/27
 */
@Getter
@AllArgsConstructor
public enum FlowPurchaseInventorySourceEnum {

    DA_YUN_HE(1,"大运河"),
    JING_DONG(2,"京东"),
    ;

    private Integer code;
    private String desc;

    public static FlowPurchaseInventorySourceEnum getFromCode(Integer code) {
        for(FlowPurchaseInventorySourceEnum e: FlowPurchaseInventorySourceEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
