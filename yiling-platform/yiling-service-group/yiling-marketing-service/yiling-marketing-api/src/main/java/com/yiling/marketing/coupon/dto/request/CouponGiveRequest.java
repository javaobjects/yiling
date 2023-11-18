package com.yiling.marketing.coupon.dto.request;

import java.util.Date;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
public class CouponGiveRequest {

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
     * 获取方式（1-运营发放；2-自动发放；3-自主领取）
     */
    private Integer getType;

    /**
     * 发放/领取人ID
     */
    private Long getUserId;

    /**
     * 发放/领取人人姓名
     */
    private String getUserName;

    /**
     * 发放/领取时间
     */
    private Date getTime;

    /**
     * 优惠券生效时间
     */
    private Date beginTime;

    /**
     * 优惠券失效时间
     */
    private Date endTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
