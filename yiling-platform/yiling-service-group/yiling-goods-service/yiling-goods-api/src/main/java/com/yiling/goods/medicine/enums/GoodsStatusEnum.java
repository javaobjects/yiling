package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态
 *
 * @author dexi.yao
 * @date 2021-05-24
 */
@Getter
@AllArgsConstructor
public enum GoodsStatusEnum {
    /**
     * 上架
     */
    UP_SHELF(1, "上架"),
    /**
     * 下架
     */
    UN_SHELF(2, "下架"),
    /**
     * 待设置
     */
    WAIT_SET(3, "待设置"),
    /**
     * 审核通过
     */
    AUDIT_PASS(4, "审核通过"),
    /**
     * 待审核
     */
    UNDER_REVIEW(5, "待审核"),
    /**
     * 驳回
     */
    REJECT(6, "驳回"),

    /**
     * 重复
     */
    REPETITION(7, "重复");

    private Integer code;
    private String  name;

    public static GoodsStatusEnum getByCode(Integer code) {
        for (GoodsStatusEnum e : GoodsStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
