package com.yiling.hmc.remind.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础服务模块错误码枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/31
 */
@Getter
@AllArgsConstructor
public enum HmcRemindErrorCode implements IErrorCode {

    PARAM_MISS_USE_TIMES_TYPE(120000, "参数缺失[用药次数]"),

    PARAM_MISS_REMIND_TIMES(120001, "参数缺失[提醒时间]"),

    PARAM_ERROR_REMIND_TIMES(120002, "时间设置不完整[提醒时间]"),

    PARAM_ERROR_REMIND_DAYS(120003, "参数错误[用药天数]"),

    ;

    private Integer code;
    private String message;
}
