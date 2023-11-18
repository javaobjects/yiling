package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/30
 */
@Data
@Accessors(chain = true)
public class QueryFlowPurchaseInventorySettlementDetailRequest extends BaseRequest{

    /**
     * 商业公司id
     */
    private Long eid;

    /**
     * 以岭品商品id
     */
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

}
