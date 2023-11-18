package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 转换规则枚举
 * @author:wei.wang
 * @date:2021/8/25
 */
@Getter
@AllArgsConstructor
public enum OrderTransitionRuleCodeEnum  {
    SALE_OUT_ORDER_OPEN_PAPER_INVOICE("AR00001", "销售出库单-应收单(开纸质普票)"),
    SALE_OUT_ORDER_OPEN_NOT_INVOICE("AR00002", "销售出库单-应收单(不开票)"),
    SALE_OUT_ORDER_OPEN_PAPER_MAJOR_INVOICE("AR00003", "销售出库单-应收单(开纸质专票)"),
    SALE_OUT_ORDER_OPEN_MAJOR_GPY_INVOICE("AR00003-gpy", "销售出库单-应收单(大运河专票)"),
    SALE_OUT_ORDER_OPEN_MARKET_INVOICE("AR00003-01", "销售出库单-应收单(营销专票带批号)"),
    SALE_OUT_ORDER_OPEN_ELECTRON_INVOICE("AR00004", "销售出库单-应收单(开电子发票)"),
    ;

    private String code;
    private String  name;

    public static OrderTransitionRuleCodeEnum getByCode(String code) {
        for (OrderTransitionRuleCodeEnum e : OrderTransitionRuleCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
