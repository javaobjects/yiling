package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议控销条件枚举
 *
 * @author: lun.yu
 * @date: 2022/2/25
 */
@Getter
@AllArgsConstructor
public enum AgreementControlSaleConditionEnum {

    /**
     * 协议控销条件
     */
    AREA(1, "区域"),
    CUSTOMER_TYPE(2, "客户类型"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementControlSaleConditionEnum getByCode(Integer code) {
        for (AgreementControlSaleConditionEnum e : AgreementControlSaleConditionEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
