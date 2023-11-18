package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 GoodsLineStatusEnum
 * @描述
 * @创建时间 2022/11/10
 * @修改人 shichen
 * @修改时间 2022/11/10
 **/
@Getter
@AllArgsConstructor
public enum GoodsLineStatusEnum {
    NOT_ENABLED(0,"未启用"),
    ENABLED(1,"启用");
    private Integer code;
    private String  name;

    public static GoodsLineStatusEnum getByCode(Integer code) {
        for (GoodsLineStatusEnum e : GoodsLineStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
