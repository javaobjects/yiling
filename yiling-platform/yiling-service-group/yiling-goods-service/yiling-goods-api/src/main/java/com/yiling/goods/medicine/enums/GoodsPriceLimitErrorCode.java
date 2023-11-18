package com.yiling.goods.medicine.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品错误码枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
@Getter
@AllArgsConstructor
public enum GoodsPriceLimitErrorCode implements IErrorCode {

    HAS_EXIST(130001, "商品控价条件已经存在，请重新设置")
    ;

    private Integer code;
    private String message;
}
