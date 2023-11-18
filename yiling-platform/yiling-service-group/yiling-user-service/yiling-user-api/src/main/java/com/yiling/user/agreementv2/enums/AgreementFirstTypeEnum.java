package com.yiling.user.agreementv2.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 甲方类型枚举
 *
 * @author: lun.yu
 * @date: 2022/2/24
 */
@Getter
@AllArgsConstructor
public enum AgreementFirstTypeEnum {

    /**
     * 甲方类型
     */
    INDUSTRIAL_PRODUCER(1, "工业生产厂家",
            ListUtil.toList(
                    AgreementTypeEnum.FIRST_AGREEMENT,
                    AgreementTypeEnum.SECOND_AGREEMENT,
                    AgreementTypeEnum.TEMP_AGREEMENT,
                    AgreementTypeEnum.KA_AGREEMENT
            )
    ),
    INDUSTRIAL_BRAND(2, "工业品牌厂家",
            ListUtil.toList(
                    AgreementTypeEnum.FIRST_AGREEMENT,
                    AgreementTypeEnum.SECOND_AGREEMENT,
                    AgreementTypeEnum.TEMP_AGREEMENT,
                    AgreementTypeEnum.KA_AGREEMENT
            )
    ),
    BUSINESS_SUPPLY(3,"商业供应商",
            ListUtil.toList(
                    AgreementTypeEnum.FIRST_AGREEMENT,
                    AgreementTypeEnum.SECOND_AGREEMENT,
                    AgreementTypeEnum.TEMP_AGREEMENT,
                    AgreementTypeEnum.BUSINESS_AGREEMENT,
                    AgreementTypeEnum.KA_AGREEMENT
            )
    ),
    AGENT(4,"代理商",
            ListUtil.toList(
                    AgreementTypeEnum.SECOND_AGREEMENT,
                    AgreementTypeEnum.TEMP_AGREEMENT
            )
    ),
    ;

    private final Integer code;
    private final String name;
    private final List<AgreementTypeEnum> agreementTypeEnumList;

    public static AgreementFirstTypeEnum getByCode(Integer code) {
        for (AgreementFirstTypeEnum e : AgreementFirstTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
