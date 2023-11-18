package com.yiling.goods.restriction.enums;

import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 RestrictionTimeTypeEnum
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Getter
@AllArgsConstructor
public enum RestrictionTimeTypeEnum {

    CUSTOM(1,"自定义"),
    EVERY_DAY(2,"每天"),
    WEEKLY(3,"每周"),
    MONTHLY(4,"每月");

    private Integer type;
    private String name;

    public static RestrictionTimeTypeEnum getByType(Integer type) {
        for (RestrictionTimeTypeEnum e : RestrictionTimeTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
