package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ERP拉取数未同步订单
 * @author:wei.wang
 * @date:2021/7/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPullErpPageRequest extends QueryPageListRequest {

    /**
     * 卖家eids
     */
    private List<Long> sellerEids;

    /**
     * 开始时间
     */
    private Date startCreateTime;

    /**
     * 结束时间
     */
    private Date endCreateTime;
}
