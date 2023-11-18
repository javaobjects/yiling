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
public enum GoodsErrorCode implements IErrorCode {

    REMOVED(120101, "商品已移除，请看看其他商品吧"),
    OFF_SHELF(120102, "商品已下架，请看看其他商品吧"),
    NOT_EXIST(120103,"商品不存在"),

    INVENTORY_NOT_ENOUGH(121103, "商品库存不足，请看看其他商品吧"),
    REPETITION(121104, "供应商商品已经存在"),
    GOODS_AUDIT_NOT_EXIST(121105, "供应商商品审核信息不存在"),
    GOODS_NOT_LINE(121106, "供应商商品没有开通产品线"),
    GOODS_NOT_SET(121107, "商品还没有设置价格和库存"),
    GOODS_NOT_SET_LINE(121108, "商品对应的客户没有开通产品线"),
    GOODS_NOT_POP_LINE(121109, "该商品不是pop商品不能开通pop产品线"),
    GOODS_INSN_REPETITION(121110, "供应商商品内码已经存在"),
    GOODS_PACKAGE_ZERO(121111,"商品销售包装必须大于0"),

    ;

    private Integer code;
    private String message;
}
