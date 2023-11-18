package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteCouponActivityEnterpriseGiveRecordRequest extends BaseRequest {

    /**
     * 操作人id
     */
    @NotNull
    private Long userId;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * ids
     */
    @NotNull
    private List<Long> ids;

    /**
     * 操作人id
     */
    @NotNull
    private Long eid;

}
