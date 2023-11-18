package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Getter
@AllArgsConstructor
public enum GoodsAuditStatusEnum {
    //待审核
    TO_AUDIT(0, "待审核"),
    //审核通过
    PASS_AUDIT(1, "审核通过"),
    //审核不通过
    NOT_PASS_AUDIT(2, "审核不通过");

    private Integer code;
    private String name;
}
