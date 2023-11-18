package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListDetailRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPaymentActivityRequest extends BaseRequest {

    /**
     * 平台标识（1-POP；2-B2B；3-销售助手；4-互联网医院；5-数据中台）
     * PlatformEnum
     */
    private Integer platform;

    /**
     * 当前用户企业ID
     */
    private Long currentEid;

    /**
     * 商品明细
     */
    private List<QueryPaymentActivityDetailRequest> goodsDetailList;

}
