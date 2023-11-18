package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退货单来源
 *
 * @author: yong.zhang
 * @date: 2021/10/26
 */
@Getter
@AllArgsConstructor
public enum ReturnSourceEnum {

    /**
     * POP-PC平台
     */
    POP_PC(1, "POP-PC平台"),
    /**
     * POP-APP平台
     */
    POP_APP(2, "POP-APP平台"),
    /**
     * B2B-APP平台
     */
    B2B_APP(3, "B2B-APP平台"),
    /**
     * 销售助手-APP平台
     */
    SA(4, "销售助手-APP平台"),
    ;

    private final Integer code;
    private final String name;

    /**
     * 根据编号查询退货单来源
     *
     * @param code  编号
     * @return  退货单来源
     */
    public static ReturnSourceEnum getByCode(Integer code) {
        for (ReturnSourceEnum e : ReturnSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
