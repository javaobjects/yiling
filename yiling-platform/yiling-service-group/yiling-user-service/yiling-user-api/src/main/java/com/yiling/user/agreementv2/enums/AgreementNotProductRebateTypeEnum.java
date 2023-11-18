package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议非商品返利类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementNotProductRebateTypeEnum {

    /**
     * 协议非商品返利类型
     */
    PARTICIPATION_FEE(1, "会务费"),
    FLOW_REBATE(2, "流向返利"),
    DAMAGED_REBATE(3,"破损返利"),
    MAINTENANCE_PRICE_REBATE(4,"维价返利"),
    CONTROL_SALES_REBATE(5,"控销返利"),
    DUE_RECEIVE_REBATE(6,"如期回款返利"),
    OTHER_REBATE(7,"其他返利"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementNotProductRebateTypeEnum getByCode(Integer code) {
        for (AgreementNotProductRebateTypeEnum e : AgreementNotProductRebateTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
