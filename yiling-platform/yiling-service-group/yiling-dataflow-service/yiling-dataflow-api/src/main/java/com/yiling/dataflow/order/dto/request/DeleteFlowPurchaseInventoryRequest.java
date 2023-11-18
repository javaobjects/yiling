package com.yiling.dataflow.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteFlowPurchaseInventoryRequest extends BaseRequest {

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 批号
     */
    private String goodsInSn;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

}
