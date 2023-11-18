package com.yiling.marketing.promotion.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 促销活动-允许企业类型
 * @author: fan.shen
 * @date: 2021/12/22
 */
@Getter
@AllArgsConstructor
public enum PromotionPermittedTypeCode implements IErrorCode {

    /**
     * 全部
     */
    ALL(1, "全部"),

    /**
     * 部分
     */
    PART(2, "部分"),
    ;

    private final Integer code;

    private final String  message;
}
