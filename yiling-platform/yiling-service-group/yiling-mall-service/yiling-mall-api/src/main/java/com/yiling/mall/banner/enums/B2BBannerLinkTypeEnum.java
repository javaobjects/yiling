package com.yiling.mall.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * B2B的Banner页面配置 1-活动详情H5 3-搜索结果页 4-商品页 5-店铺页 6-会员中心
 *
 * @author: yong.zhang
 * @date: 2022/12/2 0002
 */
@Getter
@AllArgsConstructor
public enum B2BBannerLinkTypeEnum {
    
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
     * 6-会员中心
     */
    MEMBER(6, "会员中心"),
    ;

    private final Integer code;
    private final String name;

    public static B2BBannerLinkTypeEnum getByCode(Integer code) {
        for (B2BBannerLinkTypeEnum e : B2BBannerLinkTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
