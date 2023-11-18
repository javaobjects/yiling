package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderUsePaymentActivityRequest extends BaseRequest {

    /**
     * 平台标识（1-POP；2-B2B；3-销售助手；4-互联网医院；5-数据中台）
     */
    @NotNull
    private Integer platform;

    /**
     * 当前用户的企业ID
     */
    @NotNull
    private Long currentEid;

    /**
     * 平台支付促销活动Id
     */
    private Long platformActivityId;

    /**
     * 支付促销计算规则id
     */
    private Long platformRuleId;

    /**
     * 商品SKU小计金额明细
     */
    @NotNull
    private List<OrderUsePaymentActivityGoodsRequest> goodsSkuDetailList;

}
