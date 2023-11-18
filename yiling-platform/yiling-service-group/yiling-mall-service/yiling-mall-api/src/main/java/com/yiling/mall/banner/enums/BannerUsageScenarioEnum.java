package com.yiling.mall.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner 3-B2B移动端会员中心Banner 4-B2B移动端店铺Banner
 *
 * @author: yong.zhang
 * @date: 2022/11/10
 */
@Getter
@AllArgsConstructor
public enum BannerUsageScenarioEnum {
    /**
     * 1-B2B移动端主Banner
     */
    MAIN(1, "B2B移动端主Banner"),
    /**
     * 2-B2B移动端副Banner
     */
    VICE(2, "B2B移动端副Banner"),
    /**
     * 3-B2B移动端会员中心Banner
     */
    MEMBER(3, "B2B移动端会员中心Banner"),
    /**
     * 4-B2B移动端店铺Banner
     */
    ENTERPRISE(4, "B2B移动端店铺Banner"),
    ;

    private final Integer code;
    private final String name;

    public static BannerUsageScenarioEnum getByCode(Integer code) {
        for (BannerUsageScenarioEnum e : BannerUsageScenarioEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
