package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2023/3/6
 */
@Getter
@AllArgsConstructor
public enum UnlockSaleRuleSourceEnum {
    //来源：1-手动设置2-小三批备案3-区域备案4-集采明细5-系统内置
    MANUAL(1, "手动设置"),
    SMALL_BUSINESS(2, "小三批备案"),
    REGION_FILINGS(3, "区域备案"),
    LARGE_PURCHASE(4, "集采明细"),
    SYSTEM(5, "系统内置"),
    ;

    private Integer code;
    private String  desc;

    public static UnlockSaleRuleSourceEnum getByCode(Integer code) {
        for (UnlockSaleRuleSourceEnum e : UnlockSaleRuleSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
