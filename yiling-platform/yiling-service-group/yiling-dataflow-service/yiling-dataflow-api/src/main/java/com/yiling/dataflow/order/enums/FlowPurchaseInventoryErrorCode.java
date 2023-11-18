package com.yiling.dataflow.order.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/5/27
 */
@Getter
@AllArgsConstructor
public enum  FlowPurchaseInventoryErrorCode implements IErrorCode {

    FLOW_INVENTORY_SAVE_ERROR(170001, "采购流向库存保存异常"),
    FLOW_INVENTORY_MISSING(170002, "采购流向库存扣减没有找到对应的库存信息"),
    FLOW_INVENTORY_ERROR(170003, "采购流向库存扣减错误"),
    FLOW_INVENTORY_DATA_ERROR(170004, "采购流向库存数据异常"),
    ;

    private final Integer code;
    private final String message;

}
