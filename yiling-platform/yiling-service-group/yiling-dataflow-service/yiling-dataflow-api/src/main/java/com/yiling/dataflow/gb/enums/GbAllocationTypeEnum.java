package com.yiling.dataflow.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购处理扣减分配类型枚举类
 *
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Getter
@AllArgsConstructor
public enum GbAllocationTypeEnum {

    DEDUCT(1, "扣减"),
    ADD(2, "增加"),
    ;

    private final Integer code;
    private final String message;

    public static GbAllocationTypeEnum getFromCode(Integer code) {
        for (GbAllocationTypeEnum e : GbAllocationTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }


}
