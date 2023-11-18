package com.yiling.marketing.couponactivityautogive.dto.request;

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
public class QueryCouponActivityAutoGiveRequest extends QueryPageListRequest {

    /**
     * 自动发券活动id
     */
    private Long id;

    /**
     * 自动发券活动名称
     */
    private String name;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    private Integer type;

    /**
     * 状态（1-启用；2-停用）
     */
    private Integer status;

    /**
     * 发券活动状态（1-未开始；2-进行中；3-已结束）
     */
    private Integer activityStatus;

    /**
     * 创建时间-开始
     */
    private Date beginCreateTime;

    /**
     * 创建时间-结束
     */
    private Date endCreateTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 是否重复发放(1:仅发一次 2:重复发放多次)
     */
    private Integer repeatGive;

    /**
     * 是否有推广码 1-有，2-没有
     */
    private Integer conditionPromotionCode;

}
