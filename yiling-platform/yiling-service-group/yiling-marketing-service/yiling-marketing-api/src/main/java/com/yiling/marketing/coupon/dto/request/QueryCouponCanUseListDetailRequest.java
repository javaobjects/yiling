package com.yiling.marketing.coupon.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponCanUseListDetailRequest extends BaseRequest {

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
     * 商铺优惠劵ID
     */
    private Long shopCouponId;

}
