package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderCouponUsePageRequest extends QueryPageListRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠劵ID
     */
    private Long couponId;

    /**
     * 优惠劵名称
     */
    private String couponName;

    /**
     * 用户优惠劵ID
     */
    private Long customerCouponId;

    /**
     * 优惠劵类型(1,平台劵,2,商家劵)
     */
    private Integer couponType;

    /**
     * 是否归还(1-已归还 2-未归还)
     */
    private Integer isReturn;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;


}
