package com.yiling.sjms.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购流程类型
 *
 * @author: wei.wang
 * @date: 2022/12/06
 */
@Getter
@AllArgsConstructor
public enum GbFormReviewStatusEnum {

    PASS(2, "已复核"),
    FAIL(1, "未复核"),
    ;

    private Integer code;
    private String name;

    public static GbFormReviewStatusEnum getByCode(Integer code) {
        for (GbFormReviewStatusEnum e : GbFormReviewStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
