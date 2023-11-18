package com.yiling.marketing.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Getter
@AllArgsConstructor
public enum CouponGetEnterpriseLimitTypeEnum {

    /**
     * 1-全部用户类型
     */
    ALL_ENTERPRISE(1, "全部用户类型"),
    /**
     * 2-普通用户
     */
    ENTERPRISE(2, "普通用户"),
    /**
     * 3-全部会员
     */
    MEMBER(3, "全部会员"),
    /**
     * 4-部分会员
     */
    PART_MEMBER(4, "部分会员"),
    /**
     * 5-部分用户
     */
    PART_USER(5, "部分用户"),

    /**
     * 6-新客
     */
    NEW_CUSTOMER(6, "新客"),
    ;

    private Integer code;
    private String  name;

    public static CouponGetEnterpriseLimitTypeEnum getByCode(Integer code) {
        for (CouponGetEnterpriseLimitTypeEnum e : CouponGetEnterpriseLimitTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
