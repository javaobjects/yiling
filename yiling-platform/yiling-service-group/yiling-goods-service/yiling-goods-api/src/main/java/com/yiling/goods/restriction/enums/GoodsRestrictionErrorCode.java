package com.yiling.goods.restriction.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 GoodsRestrictionErrorCode
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Getter
@AllArgsConstructor
public enum GoodsRestrictionErrorCode implements IErrorCode {
    GOODS_EXIST(125101,"商品为空"),
    CUSTOMER_EXIST(125102,"客户为空"),
    TIME_TYPE_ERROR(125103,"限购时间类型错误"),
    DATE_EXIST(125104,"时间为空"),
    TOO_MANY_CUSTOMER(125105,"限购客户数量达到上限"),
    ;
    private Integer code;
    private String message;
}
