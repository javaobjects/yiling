package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 在途订单库存类型枚举类
 *
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@Getter
@AllArgsConstructor
public enum FlowGoodsBatchTransitTypeEnum {

    IN_TRANSIT(1, "在途订单库存"),
    TERMINAL(2, "终端库存"),
    ;

    private Integer code;
    private String desc;

    public static FlowGoodsBatchTransitTypeEnum getByCode(Integer code) {
        for(FlowGoodsBatchTransitTypeEnum e: FlowGoodsBatchTransitTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
