package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**预售订单短信提醒枚举
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Getter
@AllArgsConstructor
public enum  PreSalOrderReminderTypeEnum {

    CANCEL_REMINDER("expiration_reminder", "订单取消提醒", "尊敬的客户，您购买的预售商品{}还有5分钟超时取消，请您及时付款，如已支付请忽略"),
    BALANCE_PAY_REMINDER("balance_pay_reminder", "尾款支付提醒", "尊敬的客户，您购买的预售商品{}可以支付尾款，请您及时付款，如已支付请忽略"),
    BALANCE_CANCEL_REMINDER("balance_expiration_reminder", "订单取消提醒", "尊敬的客户，您购买的预售商品{}尾款支付时间还剩24小时，超时将自动取消，请您及时付款，如已支付请忽略"),
    ;

    private String code;
    private String name;
    private String templateContent;

    public static PreSalOrderReminderTypeEnum getByCode(String code) {
        for (PreSalOrderReminderTypeEnum e : PreSalOrderReminderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
