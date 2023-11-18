package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveCouponActivityAutoGiveCouponRequest extends BaseRequest {

    /**
     * 自动发券活动ID
     */
    @NotNull
    private Long couponActivityAutoGiveId;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 自动发券数量
     */
    @NotNull
    private Integer giveNum;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 优惠券活动开始时间
     */
    private Date beginTime;

    /**
     * 优惠券活动结束时间
     */
    private Date endTime;

    /**
     * 生效类型1固定生效时间 2领券后生效
     */
    private Integer useDateType;
}
