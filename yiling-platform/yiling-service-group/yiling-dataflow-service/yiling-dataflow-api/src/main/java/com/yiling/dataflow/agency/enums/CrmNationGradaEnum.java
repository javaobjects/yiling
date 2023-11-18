package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 以岭级别
 *
 * @author: shixing.sun
 * @date: 2023/2/17
 */
@Getter
@AllArgsConstructor
public enum CrmNationGradaEnum {

    THREEJIA(1, "三级甲等"),
    THREEYI(2, "三级乙等"),
    THREEBING(3, "三级丙等"),
    TWOJIA(4, "二级甲等"),
    TWOYI(5, "二级乙等"),
    TWOBING(6, "二级丙等"),
    ONEJIA(7, "一级甲等"),
    THREETE(8, "三级特等"),
    ONEYI(9, "一级乙等"),
    YIBING(10, "一级丙等"),
    OTHER(11, "其他")
    ;

    private final Integer code;
    private final String name;

    public static CrmNationGradaEnum getByCode(Integer code) {
        for (CrmNationGradaEnum e : CrmNationGradaEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
