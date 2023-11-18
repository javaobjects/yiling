package com.yiling.goods.inventory.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddOrSubtractQtyRequest extends BaseRequest {
    private static final long serialVersionUID = -3337103042833235608L;

    /**
     * 商品goodsId
     */
    @Deprecated
    private Long goodsId;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 库存Id
     */
    private Long inventoryId;

    /**
     * 冻结数量
     */
    private Long frozenQty;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 实际库存
     */
    private Long qty;
}
