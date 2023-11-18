package com.yiling.marketing.couponactivity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityRepeatNameRequest extends BaseRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 优惠券名称
     */
    private String name;

}
