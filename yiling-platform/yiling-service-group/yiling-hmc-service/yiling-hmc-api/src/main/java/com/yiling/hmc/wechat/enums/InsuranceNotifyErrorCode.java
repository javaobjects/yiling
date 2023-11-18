package com.yiling.hmc.wechat.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参保回调通知错误码枚举类
 *
 * @author: fan.shen
 * @date: 2022/1/14
 */
@Getter
@AllArgsConstructor
public enum InsuranceNotifyErrorCode implements IErrorCode {

    PARAM_INSURANCE_ID_NULL(10000, "保险id为空"),

    PARAM_USER_ID_NULL(10001, "C端用户id为空"),

    PARAM_E_ID_NULL(10002, "eId为空"),

    PARAM_HMC_GOODS_ID_NULL(10003, "C端goodsId为空"),

    PARAM_INSURANCE_MAP_NULL(10004, "动态参数map为空"),

    PARAM_INSURANCE_COMPANY_ID_NULL(10005, "动态参数保司id为空"),

    ;

    private final Integer code;

    private final String message;
}
