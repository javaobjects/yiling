package com.yiling.hmc.settlement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
 *
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Getter
@AllArgsConstructor
public enum InsuranceSettlementStatusEnum {

    /**
     * 1-待结算
     */
    UN_SETTLEMENT(1, "待结算"),
    /**
     * 2-已结算
     */
    SETTLEMENT(2, "已结算"),
    /**
     * 3-无需结算失效单
     */
    NO_NEED_SETTLEMENT(3, "无需结算失效单"),
    /**
     * 4-预付款抵扣已结
     */
    PRE_SETTLEMENT(4, "预付款抵扣已结"),
    ;

    private final Integer code;
    private final String name;

    public static InsuranceSettlementStatusEnum getByCode(Integer code) {
        for (InsuranceSettlementStatusEnum e : InsuranceSettlementStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
