package com.yiling.marketing.promotion.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderUsePaymentActivityGoodsRequest implements java.io.Serializable {
    private static final long serialVersionUID = -449266048434826477L;

    /**
     * 店铺企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 支付方式(1-线下支付 2-账期 3-预付款 4-在线支付)
     */
    private Integer paymentMethod;

    /**
     * 商家支付促销活动Id
     */
    private Long shopActivityId;

    /**
     * 支付促销计算规则id
     */
    private Long shopRuleId;

    /**
     * 商品ID
     */
    @NotNull
    private Long goodsId;

    /**
     * 商品SKU ID
     */
    @NotNull
    private Long goodsSkuId;

    /**
     * 分摊关联Id
     */
    @NotNull
    private String relationId;

    /**
     * 商品小计金额
     */
    @NotNull
    private BigDecimal goodsAmount;

    /**
     * 商品件数
     */
    @NotNull
    private BigDecimal goodsNumber;


}
