package com.yiling.marketing.couponactivityautoget.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGetCouponRequest extends QueryPageListRequest {

    /**
     * 自主领券活动ID
     */
    @NotNull
    private Long couponActivityAutoGetId;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 可领取数量
     */
    @NotNull
    private Integer giveNum;

    /**
     * 每人每日领取数量
     */
    private Integer giveNumDaily;

    /**
     * 固定生效时间和领取后生效
     */
    private Integer useDateType;

    /**
     * 优惠券结束时间
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
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}
