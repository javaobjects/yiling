package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议核心商品组关联性枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementCoreCommodityGroupRelevanceEnum {

    /**
     * 协议核心商品组关联性
     */
    INDEPENDENT(1, "每个核心商品组相互独立"),
    COMBINATION(2, "视为核心组合"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementCoreCommodityGroupRelevanceEnum getByCode(Integer code) {
        for (AgreementCoreCommodityGroupRelevanceEnum e : AgreementCoreCommodityGroupRelevanceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
