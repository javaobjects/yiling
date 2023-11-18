package com.yiling.basic.version.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * app渠道枚举
 *
 * @author: yong.zhang
 * @date: 2021/10/27
 */
@Getter
@AllArgsConstructor
public enum AppChannelEnum {
    /**
     * 销售助手移动端渠道
     */
    SA("c1", "销售助手移动端"),
    /**
     * B2B移动端渠道枚举
     */
    B2B("c2", "B2B移动端"),
    /**
     * 医药代表端
     */
    SA_MR("c3", "医药代表端"),
    ;

    private final String code;
    private final String name;

    /**
     * 通过渠道编号获得枚举信息
     *
     * @param code 渠道编号
     * @return 渠道枚举
     */
    public static AppChannelEnum getByCode(String code) {
        for (AppChannelEnum e : AppChannelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
