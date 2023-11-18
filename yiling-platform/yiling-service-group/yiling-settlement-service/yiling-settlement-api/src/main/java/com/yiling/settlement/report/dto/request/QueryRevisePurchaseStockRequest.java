package com.yiling.settlement.report.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRevisePurchaseStockRequest extends BaseRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 以岭品id
     */
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    private Integer purchaseChannel;

}
