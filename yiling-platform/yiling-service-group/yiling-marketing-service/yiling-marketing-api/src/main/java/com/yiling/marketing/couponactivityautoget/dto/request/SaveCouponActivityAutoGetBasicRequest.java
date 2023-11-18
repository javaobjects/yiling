package com.yiling.marketing.couponactivityautoget.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
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
public class SaveCouponActivityAutoGetBasicRequest extends QueryPageListRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 客户范围
     */
    private Integer conditionEnterpriseRange;
    /**
     * 自主领券活动名称
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
     * 关联优惠券列表
     */
    @NotNull
    private List<SaveCouponActivityAutoGetCouponRequest> couponActivityAutoGetList;

    /**
     * 操作人id
     */
    private Long currentUserId;

}
