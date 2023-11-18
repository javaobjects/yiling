package com.yiling.marketing.coupon.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryCouponListPageRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 当前用户id
     */
    @NotNull
    private Long currentUserId;

    /**
     * 状态类型：1-未使用；2-已使用；3-已过期
     */
    @NotNull
    private Integer usedStatusType;

    /**
     * 平台类型：1-B2B；2-销售助手
     */
    @NotNull
    private Integer platformType;

    /**
     * 当前会员id
     */
    private Long memberId;
}
