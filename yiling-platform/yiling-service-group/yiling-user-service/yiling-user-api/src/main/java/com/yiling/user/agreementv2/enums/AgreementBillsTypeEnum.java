package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议单据类型枚举
 *
 * @author: lun.yu
 * @date: 2022-03-11
 */
@Getter
@AllArgsConstructor
public enum AgreementBillsTypeEnum {

    /**
     * 协议单据类型
     */
    CREATE(1, "协议新建"),
    UPDATE(2, "协议修改"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementBillsTypeEnum getByCode(Integer code) {
        for (AgreementBillsTypeEnum e : AgreementBillsTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
