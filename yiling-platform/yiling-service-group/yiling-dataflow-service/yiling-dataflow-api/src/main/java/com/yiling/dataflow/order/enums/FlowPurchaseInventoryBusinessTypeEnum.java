package com.yiling.dataflow.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 采购流向商品库存业务类型枚举类
 *
 * @author: houjie.sun
 * @date: 2022/6/1
 */
@Getter
@AllArgsConstructor
public enum FlowPurchaseInventoryBusinessTypeEnum {

    FLOW(1,"erp流向库存同步"),
    SETTLEMENT(2,"报表计算库存"),
    FLOW_STATISTICS_TOTAL_QUANTITY(3,"erp流向库存总数量统计"),
    SETTLEMENT_ADJUSTMENT(4,"人工调整"),
    FLOW_GOODS_RELATION(5,"以岭品关系修改"),
    ;

    private Integer code;
    private String desc;

    public static FlowPurchaseInventoryBusinessTypeEnum getFromCode(Integer code) {
        for(FlowPurchaseInventoryBusinessTypeEnum e: FlowPurchaseInventoryBusinessTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
