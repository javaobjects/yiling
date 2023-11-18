package com.yiling.settlement.b2b.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单生成状态：1-未生成 2-已生成
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Getter
@AllArgsConstructor
public enum SettGenerateStatusEnum {
    //未生成
    UN_GENERATE(1, "未生成"),
    //已生成
    GENERATED(2, "已生成");

    private Integer code;
    private String  name;

    public static SettGenerateStatusEnum getByCode(Integer code) {
        for (SettGenerateStatusEnum e : SettGenerateStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
