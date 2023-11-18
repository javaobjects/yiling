package com.yiling.hmc.wechat.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保险错误枚举
 *
 * @author fan.shen
 * @date 2022/4/7
 */
@Getter
@AllArgsConstructor
public enum InsuranceErrorCode implements IErrorCode {

    GOODS_NOT_FOUND(10200, "未查询到商品信息"),

    CALL_TK_TRIAL_ERROR(10201, "调用泰康试算接口出错"),

    SYNC_TK_ORDER_ERROR(10202, "调用泰康同步订单状态接口出错"),
    ;

    private final Integer code;
    private final String  message;
}
