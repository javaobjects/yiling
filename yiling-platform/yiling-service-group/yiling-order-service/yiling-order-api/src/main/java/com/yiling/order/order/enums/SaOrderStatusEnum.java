package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.enums
 * @date: 2021/9/18
 */
@Getter
@AllArgsConstructor
public enum SaOrderStatusEnum {

    UNAUDITED(10, "待审核"),
    UNDELIVERED(20, "待发货"),
    PARTDELIVERED(25,"部分发货"),
    DELIVERED(30, "已发货"),
    RECEIVED(40, "已收货"),
    FINISHED(100, "已完成"),
    CANCELED(-10, "已取消"),
    WAIT_PAY(-20,"待付款"),
    NOTCONFIRM(-30,"待确认"),
    ORDER_RETURN_REJECT(-40,"审核驳回"),
    UNSUBMIT(-50, "未提交"),
    ;

    private Integer code;
    private String name;

    public static SaOrderStatusEnum getByCode(Integer code) {
        for (SaOrderStatusEnum e : SaOrderStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
