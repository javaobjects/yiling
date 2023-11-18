package com.yiling.marketing.couponactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 支付方式限制（1-全部支付方式；2-部分支付方式）
 * @Description
 * @Author fan.shen
 * @Date 2022/1/27
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PayMethodLimitEnum {

    /**
     * 1-全部
     */
    ALL(1, "全部"),

    /**
     * 2-部分
     */
    PART(2, "部分"),;

    private Integer code;

    private String  name;

}
