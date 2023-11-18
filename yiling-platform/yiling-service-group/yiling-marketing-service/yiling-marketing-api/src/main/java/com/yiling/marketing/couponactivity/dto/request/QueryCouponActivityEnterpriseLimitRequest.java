package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityEnterpriseLimitRequest extends QueryPageListRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 企业id
     */
    private String eid;

    /**
     * 企业id集合
     */
    private List<Long> eids;

    /**
     * 企业名称
     */
    private String ename;

}
