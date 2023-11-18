package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteCouponActivityEnterpriseLimitRequest extends BaseRequest {

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
     * 当前企业id
     */
    private Long currentEid;

    /**
     * 平台类型：1-运营后台 2-商家后台
     */
    private Integer platformType;

}
