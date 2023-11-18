package com.yiling.goods.inventory.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 SubscriptionTypeEnum
 * @描述
 * @创建时间 2022/11/21
 * @修改人 shichen
 * @修改时间 2022/11/21
 **/
@Getter
@AllArgsConstructor
public enum SubscriptionTypeEnum {

    SELF(1,"本店库存订阅"),
    ERP(2,"erp库存订阅"),
    POP(3,"pop库存订阅");
    private Integer type;
    private String  name;

    public static SubscriptionTypeEnum getByType(Integer type) {
        for (SubscriptionTypeEnum e : SubscriptionTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
