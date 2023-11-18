package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 GoodsGroupStatusEnum
 * @描述
 * @创建时间 2023/1/13
 * @修改人 shichen
 * @修改时间 2023/1/13
 **/
@Getter
@AllArgsConstructor
public enum GoodsGroupStatusEnum {
    ENABLE(0,"启用"),
    NOT_ENABLE(1,"不启用");


    private Integer code;
    private String  name;

    public static GoodsGroupStatusEnum getByCode(Integer code) {
        for (GoodsGroupStatusEnum e : GoodsGroupStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
