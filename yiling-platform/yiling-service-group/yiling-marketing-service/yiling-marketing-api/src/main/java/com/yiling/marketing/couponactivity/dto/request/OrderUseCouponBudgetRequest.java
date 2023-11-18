package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderUseCouponBudgetRequest extends BaseRequest {

    /**
     * 平台标识（1-POP；2-B2B；3-销售助手；4-互联网医院；5-数据中台）
     * CouponPlatformTypeEnum
     */
    @NotNull
    private Integer platform;

    /**
     * 当前用户的企业ID
     */
    @NotNull
    private Long currentEid;

    /**
     * 商品SKU小计金额明细
     */
    @NotNull
    private List<OrderUseCouponBudgetGoodsDetailRequest> goodsSkuDetailList;

}
