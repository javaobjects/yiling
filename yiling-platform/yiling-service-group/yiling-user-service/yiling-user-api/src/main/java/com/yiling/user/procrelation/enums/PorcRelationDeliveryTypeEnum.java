package com.yiling.user.procrelation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配送类型：1-工业直配 2-三方配送
 *
 * @author: dexi.yao
 * @date: 2021/8/27
 */
@Getter
@AllArgsConstructor
public enum PorcRelationDeliveryTypeEnum {

    // 工业直配
    FACTORY(1, "工业直配"),
    // 三方配送
    THIRD_ENTERPRISE(2, "三方配送");

    private final Integer code;
    private final String name;

    public static PorcRelationDeliveryTypeEnum getByCode(Integer code) {
        for (PorcRelationDeliveryTypeEnum e : PorcRelationDeliveryTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
