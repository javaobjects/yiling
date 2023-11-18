package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议送货方式枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementDeliveryTypeEnum {

    /**
     * 送货方式
     */
    TO_DOOR(1, "送货上门"),
    COMPANY_DISTRIBUTION(2, "公司配送"),
    CUSTOMER_PICK_UP(3, "客户自提"),
    COMMISSIONED_DISTRIBUTION(4, "委托配送"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementDeliveryTypeEnum getByCode(Integer code) {
        for (AgreementDeliveryTypeEnum e : AgreementDeliveryTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
