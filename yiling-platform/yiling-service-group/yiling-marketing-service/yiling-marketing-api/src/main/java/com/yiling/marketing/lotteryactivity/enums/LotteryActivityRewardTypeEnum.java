package com.yiling.marketing.lotteryactivity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽奖活动奖品类型枚举类
 *
 * @author: lun.yu
 * @date: 2022-08-30
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityRewardTypeEnum {

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
     */
    REAL_GOODS(1, "真实物品"),
    VIRTUAL_GOODS(2, "虚拟物品"),
    GOODS_COUPON(3, "商品优惠券"),
    MEMBER_COUPON(4, "会员优惠券"),
    EMPTY(5, "空奖"),
    DRAW(6, "抽奖机会");
    ;

    private final Integer code;
    private final String name;

    public static LotteryActivityRewardTypeEnum getByCode(Integer code) {
        for (LotteryActivityRewardTypeEnum e : LotteryActivityRewardTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
