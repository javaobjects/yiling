package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单对账的结算单类型 1-货款&促销结算单 2-预售违约金结算单
 *
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Getter
@AllArgsConstructor
public enum SettOrderTypenum {

    //货款&促销结算单
    GOODS_OR_SALE(1, "货款&促销结算单"),
    //预售违约金结算单
    PRESALE_DEFAULT(2, "预售违约金结算单");

    private Integer code;
    private String  name;

    public static SettOrderTypenum getByCode(Integer code) {
        for (SettOrderTypenum e : SettOrderTypenum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
