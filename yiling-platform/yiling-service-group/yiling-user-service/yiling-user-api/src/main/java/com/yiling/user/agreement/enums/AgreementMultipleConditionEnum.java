package com.yiling.user.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/23
 */
@Getter
@AllArgsConstructor
public enum AgreementMultipleConditionEnum {

    //双方协议
    PAY_TYPE("payType", "支付方式"),
    //返利形式
    RESTITUTION_TYPE("restitutionType", "返利形式"),
    //回款方式
    BACK_AMOUNT_TYPE("backAmountType", "回款方式");

    private String code;

    private String name;

    public static AgreementModeEnum getByCode(Integer code) {
        for (AgreementModeEnum e : AgreementModeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
