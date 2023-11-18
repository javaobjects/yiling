package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementTypeEnum {

    /**
     * 协议类型
     */
    FIRST_AGREEMENT(1, "一级协议", "YJ"),
    SECOND_AGREEMENT(2, "二级协议","EJ"),
    TEMP_AGREEMENT(3,"临时协议","LS"),
    BUSINESS_AGREEMENT(4,"商业供货协议","SY"),
    KA_AGREEMENT(5,"KA连锁协议","KA"),
    AGENT_AGREEMENT(6,"代理商协议","DL"),
    ;

    private final Integer code;
    private final String name;
    private final String no;

    public static AgreementTypeEnum getByCode(Integer code) {
        for (AgreementTypeEnum e : AgreementTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
