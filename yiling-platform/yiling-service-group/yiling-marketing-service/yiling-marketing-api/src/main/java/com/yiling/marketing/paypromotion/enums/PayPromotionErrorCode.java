package com.yiling.marketing.paypromotion.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些支付促销的常用错误操作码
 *
 * @author: yong.zhang
 * @date: 2023/4/25 0025
 */
@Getter
@AllArgsConstructor
public enum PayPromotionErrorCode implements IErrorCode {

    /**
     * 请求得支付促销活动不满足条件
     */
    PAY_PROMOTION_NOT_MATCH(1001, "请求得支付促销活动不满足条件"),

    ;

    private final Integer code;
    private final String message;
}
