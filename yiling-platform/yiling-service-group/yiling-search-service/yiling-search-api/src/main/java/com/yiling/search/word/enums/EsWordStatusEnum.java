package com.yiling.search.word.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 EsWordStatusEnum
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Getter
@AllArgsConstructor
public enum EsWordStatusEnum {
    NORMAL(0,"正常"),
    DISABLED(1,"禁用"),
    ;
    private Integer code;
    private String  name;

    public static EsWordStatusEnum getByType(Integer code) {
        for (EsWordStatusEnum e : EsWordStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
