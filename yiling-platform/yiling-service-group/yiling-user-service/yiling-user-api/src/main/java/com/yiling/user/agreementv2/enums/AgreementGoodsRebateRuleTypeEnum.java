package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议商品返利规则设置方式枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementGoodsRebateRuleTypeEnum {

    /**
     * 商品返利规则设置方式
     */
    ALL(1, "全品设置"),
    CATEGORY(2, "分类设置"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementGoodsRebateRuleTypeEnum getByCode(Integer code) {
        for (AgreementGoodsRebateRuleTypeEnum e : AgreementGoodsRebateRuleTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
