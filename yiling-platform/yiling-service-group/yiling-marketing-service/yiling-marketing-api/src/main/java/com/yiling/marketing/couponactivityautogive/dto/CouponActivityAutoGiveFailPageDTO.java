package com.yiling.marketing.couponactivityautogive.dto;

import java.util.Date;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
public class CouponActivityAutoGiveFailPageDTO {

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}
