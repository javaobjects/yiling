package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议供销购进渠道枚举
 *
 * @author: lun.yu
 * @date: 2022/2/25
 */
@Getter
@AllArgsConstructor
public enum AgreementBuyChannelEnum {

    /**
     * 协议供销购进渠道
     */
    DIRECT_SUPPLY(1, "直供"),
    ALL_BUSINESS_BUY(2, "所有商业公司购进"),
    SPECIFIED_BUSINESS_BUY(3, "指定商业公司购进"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementBuyChannelEnum getByCode(Integer code) {
        for (AgreementBuyChannelEnum e : AgreementBuyChannelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
