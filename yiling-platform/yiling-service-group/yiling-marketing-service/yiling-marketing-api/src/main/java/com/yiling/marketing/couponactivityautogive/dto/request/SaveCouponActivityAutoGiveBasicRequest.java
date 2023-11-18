package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
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
public class SaveCouponActivityAutoGiveBasicRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 自动发券活动名称
     */
    @NotEmpty
    private String name;

    /**
     * 活动开始时间
     */
    @NotNull
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @NotNull
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

    /**
     * 关联优惠券列表
     */
    @NotNull
    private List<SaveCouponActivityAutoGiveCouponRequest> couponActivityList;

    /**
     * 操作人id
     */
    private Long currentUserId;
}
