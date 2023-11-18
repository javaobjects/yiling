package com.yiling.dataflow.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Getter
@AllArgsConstructor
public enum FlowBackupTableNamesEnum {
    FLOW_GOODS_BATCH_DETAIL("flow_goods_batch_detail"),
    FLOW_PURCHASE("flow_purchase"),
    FLOW_SALE("flow_sale"),
    ;

    private String code;

    public static FlowBackupTableNamesEnum getByCode(Integer code) {
        for(FlowBackupTableNamesEnum e: FlowBackupTableNamesEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
