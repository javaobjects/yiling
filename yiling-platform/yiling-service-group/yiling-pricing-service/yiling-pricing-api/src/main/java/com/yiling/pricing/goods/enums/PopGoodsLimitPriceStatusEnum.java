package com.yiling.pricing.goods.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 PopGoodsLimitPriceStatusEnum
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Getter
@AllArgsConstructor
public enum PopGoodsLimitPriceStatusEnum {
    NORMAL(0,"正常");
    private Integer code;
    private String  name;

    public static PopGoodsLimitPriceStatusEnum getByCode(Integer code) {
        for (PopGoodsLimitPriceStatusEnum e : PopGoodsLimitPriceStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
