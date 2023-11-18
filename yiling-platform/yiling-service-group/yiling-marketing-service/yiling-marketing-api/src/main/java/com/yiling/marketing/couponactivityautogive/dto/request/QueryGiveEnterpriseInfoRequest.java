package com.yiling.marketing.couponactivityautogive.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGiveEnterpriseInfoRequest extends BaseRequest {

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 企业ID
     */
    private Long eid;

}
