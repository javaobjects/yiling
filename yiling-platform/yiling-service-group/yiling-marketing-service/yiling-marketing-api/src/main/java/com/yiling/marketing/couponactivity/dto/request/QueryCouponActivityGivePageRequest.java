package com.yiling.marketing.couponactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryCouponActivityGivePageRequest extends QueryPageListRequest {

    /**
     * 当前企业id
     */
    private Long eid;

    /**
     * 优惠券活动id
     */
    private Long couponActivityId;
    /**
     * 自动发放/领取活动id
     */
    private Long couponActivityAutoId;

    /**
     * 类型：1-运营发放；2-自动发放；3-自主领取
     */
    @NotNull
    Integer getType;

}
