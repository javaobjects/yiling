package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Getter
@AllArgsConstructor
public enum CouponGetTypeEnum {

    // 1-运营发放；2-自动发放；3-自主领取
    /**
     * 1-运营发放  包括商品优惠券和会员优惠券
     */
    GIVE(1, "运营发放"),
    /**
     * 2-自动发放
     */
    AUTO_GIVE(2, "自动发放"),
    /**
     * 3-自主领取
     */
    AUTO_GET(3, "自主领取"),
    /**
     * 4-促销活动赠送 抽奖赠送，包括商品优惠券和会员优惠券
     */
    GIFT(4, "促销活动赠送"),
    /**
     * 5-策略满赠赠送
     */
    STRATEGY(5, "策略满赠赠送"),

    /**
     * 6-c端口核销赠送
     */
    C_SEND(6, "c端口核销赠送"),

    /**
     * 7 会员优惠券批量发放
     */
    EXCEL_BATCH_SEND(7, "会员优惠券excel批量发放"),
    /**
     * 8 积分兑换优惠券
     */
    INTEGRAL_EXCHANGE(8, "积分兑换"),
    ;

    private Integer code;
    private String  name;

    public static CouponGetTypeEnum getByCode(Integer code) {
        for (CouponGetTypeEnum e : CouponGetTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
