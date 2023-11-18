package com.yiling.order.order.dto;

import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/6/23
 */
@Data
public class OrderHistoryGoodsDTO implements java.io.Serializable {



    /**
     * 商品ID
     */

    private Long goodsId;

    /**
     * 配送商企业ID
     */

    private Long distributorEid;

    /**
     * 配送商商品id
     */
    private Long distributorGoodsId;

    /**
     * 商品skuID
     */
    private Long goodsSkuId;

}
