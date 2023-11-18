package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议附件类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementAttachmentTypeEnum {

    /**
     * 协议附件类型
     */
    AGREEMENT_ORIGINAL(1, "协议原件"),
    AGREEMENT_COPY(2, "协议复印件"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementAttachmentTypeEnum getByCode(Integer code) {
        for (AgreementAttachmentTypeEnum e : AgreementAttachmentTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
