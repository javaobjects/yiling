package com.yiling.b2b.app.order.vo;

import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 促销活动展示类型
 * @author zhigang.guo
 * @date: 2022/8/10
 */
@Getter
@AllArgsConstructor
public enum PaymentActivityShowTypeEnum {

    show(1, "有促销活动"), tip(2, "提示促销信息"), hidden(2, "隐藏");

    private final Integer type;

    private final String desc;


    public static PaymentActivityShowTypeEnum getByCode(Integer type) {
        for (PaymentActivityShowTypeEnum e : PaymentActivityShowTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
