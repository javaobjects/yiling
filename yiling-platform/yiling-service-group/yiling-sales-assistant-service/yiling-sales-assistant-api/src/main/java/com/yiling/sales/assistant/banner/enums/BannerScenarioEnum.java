package com.yiling.sales.assistant.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
 *
 * @author: yong.zhang
 * @date: 2022/1/17
 */
@Getter
@AllArgsConstructor
public enum BannerScenarioEnum {

    /**
     * 1-B2B移动端主Banner
     */
    PRIMARY(1, "B2B移动端主Banner"),

    /**
     * 2-B2B移动端副Banner
     */
    SECONDARY(2,"B2B移动端副Banner");

    private final Integer code;
    private final String  name;
}
