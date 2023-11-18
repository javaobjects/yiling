package com.yiling.settlement.yee.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2023-04-04
 */
@Getter
@AllArgsConstructor
public enum YeeSettleErrCode implements IErrorCode {

    //查询易宝子账号回款结算记录失败
    YEE_QUERY_SETTLE_FAIL(23001, "查询易宝子账号回款结算记录失败"),
    //向易宝发起请求返回的response为空
    YEE_RESPONSE_IS_NULL(23002, "向易宝发起请求返回的response为空"),

    ;


    private final Integer code;
    private final String  message;
}
