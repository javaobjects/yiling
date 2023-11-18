package com.yiling.sales.assistant.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 显示状态：1-启用 2-停用
 *
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Getter
@AllArgsConstructor
public enum BannerStatusEnum {

    /**
     * 1-启用
     */
    ENABLE(1,"启用"),

    /**
     * 2-停用
     */
    UNABLE(2,"停用"),
    ;

    private final Integer code;
    private final String  name;

}
