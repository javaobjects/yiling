package com.yiling.dataflow.wash.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 非锁客户分类
 * @author fucheng.bai
 * @date 2023/5/8
 */
@Getter
@AllArgsConstructor
public enum UnlockCustomerClassificationEnum {

    RETAIL(1, "零售机构"),
    BUSINESS(2, "商业公司"),
    MEDICAL(3, "医疗机构"),
    GOVERNMENT(4, "政府机构"),
    ;

    private Integer code;
    private String desc;

    public static String getDescByCode(Integer code) {
        return Arrays.stream(UnlockCustomerClassificationEnum.values())
                .filter(u -> u.getCode().equals(code)).map(UnlockCustomerClassificationEnum::getDesc)
                .findAny().orElse("");
    }
}
