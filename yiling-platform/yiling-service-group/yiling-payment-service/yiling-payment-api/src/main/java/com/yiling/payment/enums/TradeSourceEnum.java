package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.enums
 * @date: 2021/10/29
 */
@Getter
@AllArgsConstructor
public enum TradeSourceEnum {

    POP_PC("POP-PC", "POP-PC平台"), POP_APP("POP-APP", "POP-APP平台"), B2B_APP("B2B-APP", "B2B-APP平台"), B2B_PC("B2B-PC", "B2B-PC平台"), SA_APP("SA-APP", "销售助手-APP平台"),
    HMC("HMC","以岭健康管理中心")
    ;
    private String code;
    private String name;

    public static TradeSourceEnum getByCode(Integer code) {
        for (TradeSourceEnum e : TradeSourceEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
