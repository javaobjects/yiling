package com.yiling.basic.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章状态枚举
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    enable(1, "启用"),

    disable(2, "停用"),

    ;

    private Integer status;

    private String name;


    public static ArticleStatusEnum getByStatus(Integer status) {
        for (ArticleStatusEnum e : ArticleStatusEnum.values()) {
            if (e.getStatus().equals(status)) {
                return e;
            }
        }
        return null;
    }
}
