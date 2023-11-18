package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum  GoodsPatentEnum {
    //pop平台
    UN_PATENT(1, "非专利"),
    //B2B平台
    PATENT(2, "专利");

    private Integer code;
    private String  name;

    public static GoodsPatentEnum getByCode(Integer code) {
        for (GoodsPatentEnum e : GoodsPatentEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
