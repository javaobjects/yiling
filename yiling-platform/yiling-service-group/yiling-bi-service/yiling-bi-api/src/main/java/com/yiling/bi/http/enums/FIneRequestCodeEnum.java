package com.yiling.bi.http.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/5/8
 */
@Getter
@AllArgsConstructor
public enum FIneRequestCodeEnum {

    REQUEST_TEST(100, "测试"),
    ANALYSIS_EXCEL(101, "解析excel"),
    REMATCH_B2B_ORDER(102, "重新匹配大运河订单");

    private Integer code;
    private String  text;

    public static FIneRequestCodeEnum getFromCode(Integer code) {
        for (FIneRequestCodeEnum e : FIneRequestCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
