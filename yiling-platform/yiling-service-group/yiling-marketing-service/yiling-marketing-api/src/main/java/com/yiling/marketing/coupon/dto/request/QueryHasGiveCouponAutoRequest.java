package com.yiling.marketing.coupon.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHasGiveCouponAutoRequest extends BaseRequest {

    /**
     * 获取方式（1-运营发放；2-自动发放；3-自主领取；4-促销活动赠送）
     */
    private Integer getType;

    /**
     * 优惠券活动ID
     */
    private List<Long> couponActivityIds;

    /**
     * 平台/商家自动领取活动ID
     */
    private List<Long> autoGetIds;

    /**
     * 业务类型：1-平台自动领取活动 2-商家自动领取活动 3-自动发放
     */
    private Integer businessType;

}
