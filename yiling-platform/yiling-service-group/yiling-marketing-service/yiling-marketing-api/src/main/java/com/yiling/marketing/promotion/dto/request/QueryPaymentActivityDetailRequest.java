package com.yiling.marketing.promotion.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPaymentActivityDetailRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 支付方式（1-线下支付；2-账期；3-预付款；4-在线支付）
     * CouponPayMethodTypeEnum
     */
    @NotNull
    private Integer paymentMethod;

    /**
     * 商品ID
     */
    @NotNull
    private Long       goodsId;

    /**
     * 商品小计
     */
    @NotNull
    private BigDecimal goodsAmount;

    /**
     * 商品件数
     */
    @NotNull
    private BigDecimal  goodsNumber;


}
