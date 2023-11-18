package com.yiling.marketing.coupon.dto.request;

import java.util.List;

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
public class QueryCouponCanUseListRequest extends BaseRequest {

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
    private List<QueryCouponCanUseListDetailRequest> goodsDetailList;

    /**
     * 是否使用最优优惠劵
    */
    private Boolean isUseBestCoupon;

}
