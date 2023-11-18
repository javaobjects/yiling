package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单相关附件类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/8/2
 */
@Getter
@AllArgsConstructor
public enum OrderAttachmentTypeEnum {

    SALES_CONTRACT_FILE(1, "订单销售合同"),
    RECEIPT_FILE(2, "收货回执单"),
    ;

    private Integer code;
    private String name;

    public static OrderAttachmentTypeEnum getByCode(Integer code) {
        for (OrderAttachmentTypeEnum e : OrderAttachmentTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
