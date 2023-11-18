package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

/**
 * b2b移动端查看
 * @author:wei.wang
 * @date:2021/11/1
 */
@Data
public class OrderB2BPageRequest extends QueryPageListRequest {
    /**
     * 类型:1-待付款 2-待发货 3-待收货 4-已完成 5-已取消 6-部分发货
     */
    private Integer type;

    /**
     * 订单号或者供应商名称
     */
    private String condition;

    /**
     * 买家企业eid
     */
    private Long eid;

    /**
     * 卖家企业id
     */
    private Long sellerEid;
}
