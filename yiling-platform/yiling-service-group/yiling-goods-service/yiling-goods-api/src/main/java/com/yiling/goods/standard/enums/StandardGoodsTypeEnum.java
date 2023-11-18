package com.yiling.goods.standard.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 标准商品类型
 * @author:wei.wang
 * @date:2021/5/25
 */
@NoArgsConstructor
@AllArgsConstructor
public enum StandardGoodsTypeEnum {

    GOODS_TYPE(1, "普通药品"),

    DECOCTION_TYPE(2, "中药饮片"),

    MATERIAL_TYPE(3, "中药材"),

    DISINFECTION_TYPE(4, "消杀品"),

    HEALTH_TYPE(5, "保健食品"),

    FOODS_TYPE(6, "食品"),

    MEDICAL_INSTRUMENT_TYPE(7, "医疗器械"),

    DISPENSING_GRANULE_TYPE(8, "配方颗粒");

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static StandardGoodsTypeEnum find(Integer code) {
        if (code == null) {
            return null;
        }

        for (StandardGoodsTypeEnum element : StandardGoodsTypeEnum.values()) {
            if (element.getCode().equals(code)) {
                return element;
            }
        }
        return null;
    }
}
