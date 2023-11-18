package com.yiling.hmc.goods.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 GoodsStatusEnum
 * @描述
 * @创建时间 2022/4/2
 * @修改人 shichen
 * @修改时间 2022/4/2
 **/
@Getter
@AllArgsConstructor
public enum GoodsStatusEnum {
    /**
     * 启用
     */
    UP(1, "上架"),
    /**
     * 停用
     */
    UN(2, "下架"),
    ;

    private final Integer code;
    private final String name;

    public static GoodsStatusEnum getByCode(Integer code) {
        for (GoodsStatusEnum e : GoodsStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
