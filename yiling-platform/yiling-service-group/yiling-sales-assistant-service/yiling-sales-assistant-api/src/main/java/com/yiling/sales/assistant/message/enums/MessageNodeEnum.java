package com.yiling.sales.assistant.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *业务进度的消息节点：10-待付款 11-待审核 12-待发货 13-部分发货 14-已发货 15-已收货 16-已完成 17-已取消 20-客户信息认证失败
 * 
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Getter
@AllArgsConstructor
public enum MessageNodeEnum {
                             /**
                              * 10-待付款
                              */
                             WAITING_PAYMENT(10, "待付款"),
                             /**
                              * 11-待审核
                              */
                             WAITING_REVIEW(11, "待审核"),
                             /**
                              * 12-待发货
                              */
                             WAITING_SHIPMENT(12, "待发货"),
                             /**
                              * 13-部分发货
                              */
                             PARTIAL_SHIPMENT(13, "部分发货"),
                             /**
                              * 14-已发货
                              */
                             SHIPPED(14, "已发货"),
                             /**
                              * 15-已收货
                              */
                             RECEIVED(15, "已收货"),
                             /**
                              * 16-已完成
                              */
                             COMPLETE(16, "已完成"),
                             /**
                              * 17-已取消
                              */
                             CANCELLED(17, "已取消"),
                             /**
                              * 20-客户信息认证失败
                              */
                             CUSTOMER_AUTH_FAIL(20, "客户信息认证失败"),;

    private final Integer code;
    private final String  name;

    public static MessageNodeEnum getByCode(Integer code) {
        for (MessageNodeEnum e : MessageNodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
