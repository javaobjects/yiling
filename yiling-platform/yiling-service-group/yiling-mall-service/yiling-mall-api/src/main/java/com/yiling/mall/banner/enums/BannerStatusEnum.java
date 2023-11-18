package com.yiling.mall.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * banner状态枚举
 *
 * @author: yuecheng.chen
 * @date: 2021/6/16
 */
@Getter
@AllArgsConstructor
public enum BannerStatusEnum {
    //未开始
    NO_START(1, "未开始"),
    //进行中
    START(2, "进行中"),
    // 已结束
    END(3, "已结束");
    private Integer code;
    private String name;

    public static BannerStatusEnum getByCode(Integer code) {
        for (BannerStatusEnum e : BannerStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
