package com.yiling.order.order.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:tingwei.chen
 * @date:2021/6/22
 **/

@Data
@Accessors(chain = true)
public class OrderDetailRequest implements Serializable {
    
    /**
     * 订单明细id
     */
    private Long detailId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品skuID
     */
    private Long goodsSkuId;

    /**
     * 商品批次集合
     */
    private List<OrderDeliveryRequest> orderDeliveryList;


}


