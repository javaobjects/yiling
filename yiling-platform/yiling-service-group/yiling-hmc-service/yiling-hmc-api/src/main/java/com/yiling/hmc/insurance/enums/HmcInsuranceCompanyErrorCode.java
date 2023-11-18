package com.yiling.hmc.insurance.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yong.zhang
 * @date: 2022/4/14
 */
@Getter
@AllArgsConstructor
public enum HmcInsuranceCompanyErrorCode implements IErrorCode {

    /**
     * 保险商省市区地址不正确
     */
    SAVE_INSURANCE_COMPANY_AREA_ERROR(10113, "保险商省市区地址不正确"),

    /**
     * 此保险公司不存在
     */
    INSURANCE_COMPANY_NOT_EXISTS(10113, "此保险公司不存在"),
    ;

    private final Integer code;
    private final String message;
}
