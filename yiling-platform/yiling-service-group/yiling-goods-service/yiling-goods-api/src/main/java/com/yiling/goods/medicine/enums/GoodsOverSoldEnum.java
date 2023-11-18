package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: chen.shi
 * @date: 2021/12/21
 */
@Getter
@AllArgsConstructor
public enum GoodsOverSoldEnum {

    /**
     * 非超卖
     */
    NO_OVER_SOLD(0, "非超卖"),
    /**
     * 超卖
     */
    OVER_SOLD(1, "超卖");
    private Integer type;
    private String  name;
}
