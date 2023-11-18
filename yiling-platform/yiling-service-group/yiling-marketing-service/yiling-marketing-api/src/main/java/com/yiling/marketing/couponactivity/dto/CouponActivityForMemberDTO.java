package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * B2B-优惠券详情DTO
 *
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@Accessors(chain = true)
public class CouponActivityForMemberDTO extends BaseDTO {

    /**
     * 优惠券id
     */
    private Long activityId;
    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠内容（金额）
     */
    private BigDecimal discountValue;

    /**
     * 用券开始时间
     */
    private Date beginTime;

    /**
     * 用券结束时间
     */
    private Date endTime;

    /**
     * 1 满折 2满减
     */
    private Integer type;
}
