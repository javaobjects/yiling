package com.yiling.marketing.goodsgift.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Getter
@AllArgsConstructor
public enum GoodsGiftErrorCode implements IErrorCode {

    GOODS_GIFT_SAFE_QUANTITY_ERROR(30111, "赠品安全库数量应不小于修改前原来数据"),
    GOODS_GIFT_NOT_EXIST(30110, "赠品安全库数量应不小于修改前原来数据"),
    GOODS_GIFT_QUANTITY_ERROR(30112, "赠品库数量不正确"),
    GOODS_GIFT_JOIN_ACTIVITY_NOT_UPDATE(30113,"参与活动不能修改"),
    GOODS_GIFT_PRICE_EMPTY(30114, "赠品价值不能为空"),
    GOODS_GIFT_PRICE_NEGATIVE(30115, "赠品价值不能为零或者负数"),
    GOODS_GIFT_PRICE_SCALE_TOO_LONG(30116, "赠品价值不能超过两位小数"),
    ;

    private final Integer code;
    private final String  message;
}
