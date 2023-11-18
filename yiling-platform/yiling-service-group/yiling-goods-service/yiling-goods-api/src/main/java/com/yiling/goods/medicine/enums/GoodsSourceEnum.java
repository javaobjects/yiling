package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/2
 */
@Getter
@AllArgsConstructor
public enum GoodsSourceEnum {
    //平台录入
    PLATFORM(1, "录入"),
    //平台导入
    IMPORT(2, "导入"),
    //ERP平台
    ERP(3, "ERP"),
    //互联网医院
    HOSPITAL(4, "互联网医院");

    private Integer code;
    private String  name;


    public static GoodsSourceEnum getByCode(Integer code) {
        for (GoodsSourceEnum e : GoodsSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
