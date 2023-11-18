package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 GoodsSkuStatusEnum
 * @描述
 * @创建时间 2022/3/11
 * @修改人 shichen
 * @修改时间 2022/3/11
 **/
@Getter
@AllArgsConstructor
public enum GoodsSkuStatusEnum {
    NORMAL(0,"正常"),
    DISABLE(1,"停用"),
    HIDE(2,"隐藏");
    private Integer code;
    private String  name;

    public static GoodsSkuStatusEnum getByCode(Integer code) {
        for (GoodsSkuStatusEnum e : GoodsSkuStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
