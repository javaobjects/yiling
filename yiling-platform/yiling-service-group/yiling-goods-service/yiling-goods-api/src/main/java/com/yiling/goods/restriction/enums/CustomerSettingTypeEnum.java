package com.yiling.goods.restriction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 CustomerSettingTypeEnum
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Getter
@AllArgsConstructor
public enum CustomerSettingTypeEnum {
    ALL(0,"全部客户"),
    SOME(1,"部分客户");

    private Integer type;
    private String name;

    public static CustomerSettingTypeEnum getByType(Integer type) {
        for (CustomerSettingTypeEnum e : CustomerSettingTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
