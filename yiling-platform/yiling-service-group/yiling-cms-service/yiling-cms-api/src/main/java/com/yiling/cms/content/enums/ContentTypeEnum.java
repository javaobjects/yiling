package com.yiling.cms.content.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内容类型枚举
 * @author: gxl
 * @date: 2022/6/13
 */
@Getter
@AllArgsConstructor
public enum ContentTypeEnum {
    ARTICLE(1,"文章"),
    VIDEO(2,"视频"),
    ;

    private final Integer code;
    private final String name;
}
