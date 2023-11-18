package com.yiling.cms.read.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 我的阅读 内容类型枚举
 * @author: gxl
 * @date: 2022/7/28
 */
@Getter
@AllArgsConstructor
public enum ReadTypeEnums {
    ARTICLE(1,"文章"),
    VIDEO(2,"视频"),
    DOCUMENT(3,"文献"),
    MEETING(4,"会议"),
    ;

    private final Integer code;
    private final String name;
}
