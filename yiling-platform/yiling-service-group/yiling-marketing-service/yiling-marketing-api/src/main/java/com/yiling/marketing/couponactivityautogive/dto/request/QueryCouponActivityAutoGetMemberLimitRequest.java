package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityAutoGetMemberLimitRequest extends QueryPageListRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 自主领取活动ID
     */
    @NotNull
    private Long couponActivityAutoGetId;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业id
     */
    private List<Long> eids;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

}
