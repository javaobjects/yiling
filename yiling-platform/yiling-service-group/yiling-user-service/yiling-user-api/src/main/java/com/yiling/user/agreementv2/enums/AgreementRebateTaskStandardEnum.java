package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议返利任务量标准枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateTaskStandardEnum {

    /**
     * 协议返利任务量标准
     */
    SALES(1, "销售"),
    BUY(2, "购进"),
    PAY_AMOUNT(3, "付款金额"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebateTaskStandardEnum getByCode(Integer code) {
        for (AgreementRebateTaskStandardEnum e : AgreementRebateTaskStandardEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
