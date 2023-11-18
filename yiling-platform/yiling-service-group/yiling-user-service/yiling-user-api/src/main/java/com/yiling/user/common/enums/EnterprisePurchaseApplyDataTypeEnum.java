package com.yiling.user.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业客户采购关系申请数据类型 枚举
 * @author lun.yu
 * @date: 2022/01/18
 */
@Getter
@AllArgsConstructor
public enum EnterprisePurchaseApplyDataTypeEnum {

    /**
     * 查询的数据类型
     */
    BUY(1, "查询采购商列表"),
    SELL(2, "查询供应商列表"),
    ;

    private final Integer code;
    private final String name;

    public static EnterprisePurchaseApplyDataTypeEnum getByCode(Integer code) {
        for (EnterprisePurchaseApplyDataTypeEnum e : EnterprisePurchaseApplyDataTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
