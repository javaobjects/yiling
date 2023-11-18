package com.yiling.hmc.enterprise.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保险公司相关错误枚举
 *
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Getter
@AllArgsConstructor
public enum HmcEnterpriseErrorCode implements IErrorCode {

    /**
     * 此商家未开通'C端药+险的销售与药品兑付'功能请先设置
     */
    ENTERPRISE_ACCOUNT_TYPE_ERROR(10111, "此商家未开通'C端药+险的销售与药品兑付'功能请先设置"),

    PHARMACY_NOT_FOUND_ERROR(10112, "根据IhEid未获取到配送商"),
    ;

    private final Integer code;
    private final String message;
}
