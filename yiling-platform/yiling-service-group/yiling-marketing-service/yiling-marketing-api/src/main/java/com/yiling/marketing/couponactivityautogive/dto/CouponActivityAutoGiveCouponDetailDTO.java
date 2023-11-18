package com.yiling.marketing.couponactivityautogive.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/22
 */
@Data
@Accessors(chain = true)
public class CouponActivityAutoGiveCouponDetailDTO implements java.io.Serializable{

    private static final long serialVersionUID = -4708413687211671130L;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 自动发券数量
     */
    private Integer giveNum;

    /**
     * 推广企业id
     */
    private Long promotionEid;

}
