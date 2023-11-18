package com.yiling.goods.inventory.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/7/5
 */
@Getter
@AllArgsConstructor
public enum InventoryErrorCode implements IErrorCode {

    INVENTORY_MISSING(150001, "库存扣减错误"),
    INVENTORY_SKU_MISSING(150002, "库存扣减没有找到对应的库存信息"),
    INVENTORY_SEND_ERROR(150003, "库存消息发送错误"),
    SUBSCRIPTION_REPEAT(150004,"库存订阅重复");

    private final Integer code;
    private final String message;
}
