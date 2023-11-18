package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * KA协议类型枚举
 *
 * @author: lun.yu
 * @date: 2022-03-09
 */
@Getter
@AllArgsConstructor
public enum KaAgreementTypeEnum {

    /**
     * KA协议类型
     */
    UNIFIED_SIGN_PAY(1, "统谈统签，统一支付"),
    UNIFIED_SIGN_SEPARATE_PAY(2, "统谈统签，分开支付"),
    SEPARATE_SIGN_SEPARATE_PAY(3,"统谈分签，分开支付"),
    ALONE_SIGN(4,"单独签订"),
    ;

    private final Integer code;
    private final String name;

    public static KaAgreementTypeEnum getByCode(Integer code) {
        for (KaAgreementTypeEnum e : KaAgreementTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
