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
public class QueryActivityDetailRequest extends BaseRequest {
    /**
     * 卡包id
     */
    private Long couponId;

    /**
     * 当前企业id,购买会员的eid
     */
    private Long eid;

    /**
     * 会员规格id，不是会员id
     */
    private Long memberId;
}
