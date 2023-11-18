package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2023/3/6
 */
@Getter
@AllArgsConstructor
public enum FlowTypeEnum {
    //1-采购 2-销售 3-库存
    PURCHASE(1, "采购", "P"),
    SALE(2, "销售", "S"),
    GOODS_BATCH(3, "库存", "I"),
    ;

    private Integer code;
    private String  desc;
    private String prefix;

    public static FlowTypeEnum getByCode(Integer code) {
        for(FlowTypeEnum e: FlowTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
