package com.yiling.goods.inventory.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  库存日志记录
 * @author: zhigang.guo
 * @date: 2021/09/2
 */
@Getter
@AllArgsConstructor
public enum InventoryLogEnum {

    INSTOCK("instock", "库存入库"),
    MODIFY("modify", "库存调整"),
    SUBSCRIPTION_MODIFY("subscription_modify", "库存订阅调整"),
    FROZEN("frozen", "库存冻结"),
    UNFROZEN("unfrozen", "取消冻结"),
    BATCH_UNFROZEN("batch_unfrozen", "批量退还冻结"),
    OUTSTOCK("outstock", "库存出库"),
    BACKSTOCK("backstock", "反审核出库")
    ;
    private final String code;
    private final String message;


    public static InventoryLogEnum getByCode(Integer code) {

        for (InventoryLogEnum e : InventoryLogEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }

        return null;
    }
}
