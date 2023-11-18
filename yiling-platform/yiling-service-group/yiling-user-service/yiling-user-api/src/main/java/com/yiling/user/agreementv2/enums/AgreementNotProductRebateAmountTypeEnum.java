package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议非商品返利金额类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementNotProductRebateAmountTypeEnum {

    /**
     * 协议非商品返利金额类型
     */
    SALE(1, "销售金额"),
    BUY(2, "购进金额"),
    PAY(3,"付款金额"),
    FIXED(4,"固定金额"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementNotProductRebateAmountTypeEnum getByCode(Integer code) {
        for (AgreementNotProductRebateAmountTypeEnum e : AgreementNotProductRebateAmountTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
