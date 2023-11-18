package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议审核状态枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementAuthStatusEnum {

    /**
     * 协议审核状态
     */
    WAITING(1, "待审核"),
    PASS(2, "审核通过"),
    REJECT(3,"审核驳回"),
    ARCHIVE(4,"已归档"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementAuthStatusEnum getByCode(Integer code) {
        for (AgreementAuthStatusEnum e : AgreementAuthStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
