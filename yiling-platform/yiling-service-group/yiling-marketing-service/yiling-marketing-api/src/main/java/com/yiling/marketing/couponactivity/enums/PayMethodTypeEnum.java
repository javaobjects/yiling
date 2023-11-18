package com.yiling.marketing.couponactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 支付方式(1-在线支付；2-货到付款；3-账期)
 * @Description
 * @Author fan.shen
 * @Date 2022/1/27
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PayMethodTypeEnum {

    /**
     * 1-在线支付
     */
    ONLINE_PAY(1, "在线支付"),

    /**
     * 2-货到付款
     */
    PAY_ON_DELIVERY(2, "货到付款"),

    /**
     * 3-账期
     */
    SETTLEMENT(3, "账期"),

    ;

    private Integer code;

    private String  name;


    public static String getByCode(Integer code) {
        for (PayMethodTypeEnum e : PayMethodTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }

}
