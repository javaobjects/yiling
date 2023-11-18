package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业采购关系申请枚举
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Getter
@AllArgsConstructor
public enum EnterprisePurchaseApplyStatusEnum {

    //审核状态：1-待审核 2-已建采 3-已驳回
    WAITING(1, "待审核"),
    ESTABLISHED(2, "已建采"),
    REJECTED(3, "已驳回"),
    ;

    private final Integer code;
    private final String name;

    public static EnterprisePurchaseApplyStatusEnum getByCode(Integer code) {
        for (EnterprisePurchaseApplyStatusEnum e : EnterprisePurchaseApplyStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
