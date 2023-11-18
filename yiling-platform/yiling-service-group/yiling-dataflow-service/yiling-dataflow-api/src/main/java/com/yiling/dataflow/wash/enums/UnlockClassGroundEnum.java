package com.yiling.dataflow.wash.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fucheng.bai
 * @date 2023/5/8
 */
@Getter
@AllArgsConstructor
public enum UnlockClassGroundEnum {

    RULE(1, "规则"),
    ARTIFICIAL(2, "人工"),
    ;

    private Integer code;
    private String desc;

    public static String getDescByCode(Integer code) {
        return Arrays.stream(UnlockClassGroundEnum.values())
                .filter(u -> u.getCode().equals(code)).map(UnlockClassGroundEnum::getDesc)
                .findAny().orElse("");
    }
}
