package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityGiveFailRequest extends QueryPageListRequest {

    /**
     * 自动发券活动ID
     */
    private Long couponActivityAutoGiveId;

    /**
     * 自动发券活动名称
     */
    private String couponActivityAutoGiveName;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 失败原因
     */
    private String faileReason;

    /**
     * 创建时间-开始
     */
    private Date createBeginTime;

    /**
     * 创建时间-结束
     */
    private Date createEndTime;

}
