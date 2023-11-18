package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账期日志类型枚举
 *
 * @author: lun.yu
 * @date: 2021/8/23
 */
@Getter
@AllArgsConstructor
public enum PaymentDaysLogTypeEnum {

    // 业务类型：1-支付 2-退款 3-还款 4-反审变更 5.临时额度 6.票折金额
    PAY(1, "支付"),
    REFUND(2, "驳回退货单退款"),
    REPAYMENT(3, "还款"),
    CONVERT_CHANGE(4, "反审变更"),
    TEMP_AMOUNT(5, "临时额度"),
    TICKET_DISCOUNT_AMOUNT(6, "票折金额");

    private final Integer code;
    private final String name;

    public static PaymentDaysLogTypeEnum getByCode(Integer code) {
        for (PaymentDaysLogTypeEnum e : PaymentDaysLogTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
