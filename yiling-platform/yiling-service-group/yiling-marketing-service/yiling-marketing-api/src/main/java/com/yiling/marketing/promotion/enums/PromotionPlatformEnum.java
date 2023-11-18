package com.yiling.marketing.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 促销活动枚举类
 * @Author fan.shen
 * @Date 2021/12/20
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PromotionPlatformEnum {

    B2B(1, "B2B"),

    SALES_ASSIST(2, "销售助手"),

    ;

    private Integer type;

    private String  name;


}
