package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单来源枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum OrderReturnStatusEnum {
                                   /**
                                    * 待审核
                                    */
                                   ORDER_RETURN_PENDING(1, "待审核"),
                                   /**
                                    * 审核通过
                                    */
                                   ORDER_RETURN_PASS(2, "审核通过"),
                                   /**
                                    * 审核驳回
                                    */
                                   ORDER_RETURN_REJECT(3, "审核驳回");

    private final Integer code;
    private final String  name;

    /**
     * 根据编号查询枚举信息
     * 
     * @param code  编号
     * @return  枚举信息
     */
    public static OrderReturnStatusEnum getByCode(Integer code) {
        for (OrderReturnStatusEnum e : OrderReturnStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
