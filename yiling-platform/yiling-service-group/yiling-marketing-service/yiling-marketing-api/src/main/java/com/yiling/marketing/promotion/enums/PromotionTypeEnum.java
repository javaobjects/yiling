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
public enum PromotionTypeEnum {

    /**
     * 满赠
     */
    FULL_GIFT(1, "满赠"),

    /**
     * 特价
     */
    SPECIAL_PRICE(2, "特价"),

    /**
     * 秒杀
     */
    SECOND_KILL(3, "秒杀"),

    /**
     * 组合包
     */
    COMBINATION_PACKAGE(4, "组合包"),

    ;

    private Integer type;

    private String  name;

    public static PromotionTypeEnum getByType(Integer type) {
        for (PromotionTypeEnum e : PromotionTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 判断是否满赠活动
     * @param type
     * @return
     */
    public static boolean isFullGift(Integer type) {
        if(FULL_GIFT.getType().equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否特价或者秒杀活动
     * @param type
     * @return TRUE - 是，FALSE - 否
     */
    public static boolean isSecKillOrSpecialPrice(Integer type) {
        if(SPECIAL_PRICE.getType().equals(type) || SECOND_KILL.getType().equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否特价或者秒杀或者组合包活动
     * @param type
     * @return TRUE - 是，FALSE - 否
     */
    public static boolean isSecKillOrSpecialPriceOrCombinationPackage(Integer type) {
        if (SPECIAL_PRICE.getType().equals(type) || SECOND_KILL.getType().equals(type) || COMBINATION_PACKAGE.getType().equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否组合包活动
     * @param type
     * @return TRUE - 是，FALSE - 否
     */
    public static boolean isCombinationPackage(Integer type) {
        if (COMBINATION_PACKAGE.getType().equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否特价活动
     * @param type
     * @return TRUE - 是，FALSE - 否
     */
    public static boolean isSpecialPrice(Integer type) {
        if(SPECIAL_PRICE.getType().equals(type)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否秒杀活动
     * @param type
     * @return TRUE - 是，FALSE - 否
     */
    public static boolean isSecKill(Integer type) {
        if(SECOND_KILL.getType().equals(type)) {
            return true;
        }
        return false;
    }

}
