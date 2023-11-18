package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议返利兑付方式枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementRebateCashTypeEnum {

    /**
     * 协议返利兑付方式
     */
    WIRE_TRANSFER(1, "电汇"),
    PUSH_RED(2, "冲红"),
    TICKET_DISCOUNT(3, "票折"),
    EASY_CREDIT(4, "易贷"),
    ACCEPTANCE_THREE_MONTHS(5, "3个月承兑"),
    ACCEPTANCE_SIX_MONTHS(6, "6个月承兑"),
    OTHER(7, "其他"),
    CHEQUE(8, "支票"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementRebateCashTypeEnum getByCode(Integer code) {
        for (AgreementRebateCashTypeEnum e : AgreementRebateCashTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
