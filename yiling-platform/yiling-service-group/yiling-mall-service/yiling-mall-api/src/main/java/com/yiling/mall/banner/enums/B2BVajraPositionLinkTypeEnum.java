package com.yiling.mall.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 页面配置1-活动详情H5 3-搜索结果页 4-商品页 5-店铺页 6-领券中心 7-活动中心 8-会员中心
 *
 * @author: yong.zhang
 * @date: 2022/12/5 0005
 */
@Getter
@AllArgsConstructor
public enum B2BVajraPositionLinkTypeEnum {

    /**
     * 1-活动详情H5
     */
    H5(1, "商品"),
    /**
     * 3-搜索结果页
     */
    SEARCH(3, "搜索结果页"),
    /**
     * 4-商品页
     */
    GOODS(4, "商品页"),
    /**
     * 5-店铺页
     */
    SHOP(5, "店铺页"),
    /**
     * 6-领券中心
     */
    COUPON(6, "领券中心"),
    /**
     * 7-活动中心
     */
    ACTIVITY(7, "活动中心"),
    /**
     * 8-会员中心
     */
    MEMBER(8, "会员中心"),
    ;

    private final Integer code;
    private final String name;

    public static B2BVajraPositionLinkTypeEnum getByCode(Integer code) {
        for (B2BVajraPositionLinkTypeEnum e : B2BVajraPositionLinkTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
