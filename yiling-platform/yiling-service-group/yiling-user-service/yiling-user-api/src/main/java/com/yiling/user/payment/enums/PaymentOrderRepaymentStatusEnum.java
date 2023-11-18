package com.yiling.user.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账期订单还款状态枚举
 * @author: lun.yu
 * @date: 2021/8/5
 */
@Getter
@AllArgsConstructor
public enum PaymentOrderRepaymentStatusEnum {

    // 未还款
    NO_REPAYMENT(1, "未还款"),
    // 部分还款
    PART_REPAYMENT(2, "部分还款"),
    // 全部还款
    ALL_REPAYMENT(3, "全部还款");

    private Integer code;
    private String name;

    public static PaymentOrderRepaymentStatusEnum getByCode(Integer code) {
        for (PaymentOrderRepaymentStatusEnum e : PaymentOrderRepaymentStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
