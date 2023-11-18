package com.yiling.goods.inventory.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 InventorySubscriptionStatusEnum
 * @描述
 * @创建时间 2022/7/25
 * @修改人 shichen
 * @修改时间 2022/7/25
 **/
@Getter
@AllArgsConstructor
public enum InventorySubscriptionStatusEnum {
    NORMAL(0,"正常"),
    DISABLE(1,"停用");
    private Integer code;
    private String  name;

    public static InventorySubscriptionStatusEnum getByCode(Integer code) {
        for (InventorySubscriptionStatusEnum e : InventorySubscriptionStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
