package com.yiling.mall.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * banner链接类型枚举
 *
 * @author: yuecheng.chen
 * @date: 2021/6/15
 */
@Getter
@AllArgsConstructor
public enum BannerLinkTypeEnum {
    //商品
    GOODS(1, "商品"),
    //链接
    URL(2, "链接");

    private Integer code;
    private String name;

    public static BannerLinkTypeEnum getByCode(Integer code) {
        for (BannerLinkTypeEnum e : BannerLinkTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
