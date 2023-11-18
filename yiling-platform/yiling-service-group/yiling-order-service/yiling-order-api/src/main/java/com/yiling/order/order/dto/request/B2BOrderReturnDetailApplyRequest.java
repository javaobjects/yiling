package com.yiling.order.order.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 申请退货请求参数-退货明细
 *
 * @author: yong.zhang
 * @date: 2021/10/19
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnDetailApplyRequest implements Serializable {

    /**
     * 订单明细detailId
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

    private List<B2BOrderReturnDetailBatchApplyRequest> orderReturnDetailBatchList;
}
