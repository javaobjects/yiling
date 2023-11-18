package com.yiling.hmc.insurance.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保险相关错误枚举
 *
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Getter
@AllArgsConstructor
public enum HmcInsuranceErrorCode implements IErrorCode {

    /**
     * 此药品已经在此保司下存，不允许重复添加
     */
    INSURANCE_DETAIL_EXISTS(10111, "此药品已经在此保司下存，不允许重复添加"),
    /**
     * 保险商品已经参与其它保险
     */
    INSURANCE_DETAIL_EXISTS_ERROR(10114, "保险商品在别的保险中已经被启用，请确保其它保险里面保险商品没有启用！"),
    /**
     * 检测到有相同保司标识的服务项开启中
     */
    INSURANCE_IDENTIFICATION_REPEAT(10115, "检测到有相同保司标识的服务项开启中，不允许开启!"),
    ;

    private final Integer code;
    private final String message;
}
