package com.yiling.mall.agreement.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/7/5
 */
@Getter
@AllArgsConstructor
public enum  AgreementErrorCode implements IErrorCode {

    AGREEMENT_MISSING(140001, "没有对应的协议信息"),
    AGREEMENT_TYPE_MISSING(140002, "协议类型不对"),
    AGREEMENT_ACOUNT_MISSING(140003, "EAS账号信息不存在或者被禁用"),
    AGREEMENT_CONDITION_MISSING(140004, "协议条件信息为空"),
    AGREEMENT_CASH_MISSING(140005, "协议还没有计算不能兑付"),

    GOODS_NOT_FIND(140006, "配置商品信息不在主体商品范围内"),
    AGREEMENT_EXIST(140007, "同一维度的协议只能存在一份"),

    AGREEMENT_GOODS_NOT_FIND(140008, "补充协议商品不再主协议内"),
    AGREEMENT_GOODS_TYPE_INVALID(140009, "补充协议商品类型不匹配"),
    ;

    private final Integer code;
    private final String message;
}
