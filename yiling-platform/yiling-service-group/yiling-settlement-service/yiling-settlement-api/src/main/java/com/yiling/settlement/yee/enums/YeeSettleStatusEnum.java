package com.yiling.settlement.yee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算订单状态： 1-结算成功 2-失败 3-待处理 4-待出款 5-结算异常 6-银行处理中
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Getter
@AllArgsConstructor
public enum YeeSettleStatusEnum {
    //结算成功
    SUCCESS(1, "结算成功","SUCCESS"),
    //失败
    FAIL(2, "结算失败","FAIL"),
    //待处理
    PROCESS(3, "待处理","PROCESS"),
    //待出款
    BEFORE_REMIT(4, "待出款","BEFORE_REMIT"),
    //结算异常
    SETTLE_FAIL(5, "结算异常","SETTLE_FAIL"),
    //银行处理中
    REMITTING(6, "银行处理中","REMITTING");

    private Integer code;
    private String  name;
    private String  yeeName;

    public static YeeSettleStatusEnum getByCode(Integer code) {
        for (YeeSettleStatusEnum e : YeeSettleStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static YeeSettleStatusEnum getByYeeName(String yeeName) {
        for (YeeSettleStatusEnum e : YeeSettleStatusEnum.values()) {
            if (e.getYeeName().equals(yeeName)) {
                return e;
            }
        }
        return null;
    }
}
