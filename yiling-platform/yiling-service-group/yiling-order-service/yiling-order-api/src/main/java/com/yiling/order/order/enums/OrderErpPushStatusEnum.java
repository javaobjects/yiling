package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 订单推送类型枚举
 *
 * @author: wei.wang
 * @date: 2021/8/5
 */
@Getter
@AllArgsConstructor
public enum OrderErpPushStatusEnum {

    NOT_PUSH(1, "未推送"),
    PUSH_SUCCESS(2, "推送成功"),
    PUSH_FAIL(3, "推送失败"),
    EXTRACT_SUCCESS(4, "提取成功"),
    EXTRACT_FAIL(5, "提取失败"),
    READ_SUCCESS(6, "读取成功")
    ;

    private Integer code;
    private String name;

    public static OrderErpPushStatusEnum getByCode(Integer code) {
        for (OrderErpPushStatusEnum e : OrderErpPushStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
