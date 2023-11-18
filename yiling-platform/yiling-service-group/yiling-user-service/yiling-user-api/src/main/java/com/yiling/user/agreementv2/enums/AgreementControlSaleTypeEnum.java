package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议控销类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/25
 */
@Getter
@AllArgsConstructor
public enum AgreementControlSaleTypeEnum {

    /**
     * 协议控销类型
     */
    NONE(1, "无"),
    BLACKLIST(2, "黑名单"),
    WHITELIST(3, "白名单"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementControlSaleTypeEnum getByCode(Integer code) {
        for (AgreementControlSaleTypeEnum e : AgreementControlSaleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
