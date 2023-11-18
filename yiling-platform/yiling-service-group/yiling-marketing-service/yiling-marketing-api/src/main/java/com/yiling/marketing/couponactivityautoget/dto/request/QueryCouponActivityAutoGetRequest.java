package com.yiling.marketing.couponactivityautoget.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityAutoGetRequest extends QueryPageListRequest {

    /**
     * 自主领券活动id
     */
    private Long id;

    /**
     * 自主领券活动名称
     */
    private String name;

    /**
     * 状态（1-启用；2-停用）
     */
    private Integer status;

    /**
     * 领券活动状态（1-未开始；2-进行中；3-已结束）
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

}
