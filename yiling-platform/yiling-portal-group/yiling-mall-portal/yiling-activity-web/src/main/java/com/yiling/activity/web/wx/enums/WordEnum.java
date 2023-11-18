package com.yiling.activity.web.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 词条枚举类
 * @Author fan.shen
 * @Date 2022/3/28
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WordEnum {


    /**
     * 兔年大吉
     */
    RABBIT_YEAR_HAPPY(1, "兔年大吉"),


    ;

    private Integer type;

    private String  name;

    public static WordEnum getByType(Integer type) {
        for (WordEnum e : WordEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
